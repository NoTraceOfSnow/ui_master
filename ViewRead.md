#### View绘制流程

##### 入口类ActivityThread


```java
    //ActivityThread
    public final class ActivityThread {
         public void handleMessage(Message msg) {
               switch (msg.what) {
                case LAUNCH_ACTIVITY: {
                    //省略部分代码
                     handleLaunchActivity(r, null, "LAUNCH_ACTIVITY");
                }
                //省略部分代码
               }
         }
      private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent, String reason) {
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        //省略部分代码
        Activity a = performLaunchActivity(r, customIntent);

        if (a != null) {
            r.createdConfig = new Configuration(mConfiguration);
            reportSizeConfigurations(r);
            Bundle oldState = r.state;
            handleResumeActivity(r.token, false, r.isForward,
                    !r.activity.mFinished && !r.startsNotResumed, r.lastProcessedSeq, reason);
            //省略部分代码
        } else {
            //省略部分代码
        } 
       }
       
     final void handleResumeActivity(IBinder token,
            boolean clearHide, boolean isForward, boolean reallyResume, int seq, String reason) {
        //省略部分代码

        // TODO Push resumeArgs into the activity for consideration
        r = performResumeActivity(token, clearHide, reason);

        if (r != null) {
            final Activity a = r.activity;
             //省略部分代码
            if (r.window == null && !a.mFinished && willBeVisible) {
                r.window = r.activity.getWindow();
                View decor = r.window.getDecorView();
                decor.setVisibility(View.INVISIBLE);
                ViewManager wm = a.getWindowManager();
                WindowManager.LayoutParams l = r.window.getAttributes();
                a.mDecor = decor;
                l.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
                l.softInputMode |= forwardBit;
                //省略部分代码
                if (a.mVisibleFromClient) {
                    if (!a.mWindowAdded) {
                        a.mWindowAdded = true;
                        //WindowManager添加view
                        wm.addView(decor, l);
                    } else {
                        // The activity will get a callback for this {@link LayoutParams} change
                        // earlier. However, at that time the decor will not be set (this is set
                        // in this method), so no action will be taken. This call ensures the
                        // callback occurs with the decor set.
                        a.onWindowAttributesChanged(l);
                    }
                }
                 //省略部分代码
            }
        }
      }
    }
    //ViewManager初始化
    //调用了Activity的windowManager
    public class Activity extends ContextThemeWrapper
        implements LayoutInflater.Factory2,
        Window.Callback, KeyEvent.Callback,
        OnCreateContextMenuListener, ComponentCallbacks2,
        Window.OnWindowDismissedCallback, WindowControllerCallback,
        AutofillManager.AutofillClient {
        
        private WindowManager mWindowManager;
        
        private Window mWindow;
        
      final void attach(Context context, ActivityThread aThread,
            Instrumentation instr, IBinder token, int ident,
            Application application, Intent intent, ActivityInfo info,
            CharSequence title, Activity parent, String id,
            NonConfigurationInstances lastNonConfigurationInstances,
            Configuration config, String referrer, IVoiceInteractor voiceInteractor,
            Window window, ActivityConfigCallback activityConfigCallback) {
       //省略部分代码
        mWindowManager = mWindow.getWindowManager();
        //省略部分代码
        }
         /** Retrieve the window manager for showing custom windows. */
        public WindowManager getWindowManager() {
            return mWindowManager;
        }
    
    }
    
    //调用Window的getWindowManager
    
    public abstract class Window {
        private WindowManager mWindowManager;
        public void setWindowManager(WindowManager wm, IBinder appToken, String appName,
           boolean hardwareAccelerated) {
             mWindowManager = ((WindowManagerImpl)wm).createLocalWindowManager(this);
        }
        public WindowManager getWindowManager() {
            return mWindowManager;
        }
    }
    
    //调用WindowManagerImpl中的createLocalWindowManager
    public final class WindowManagerImpl implements WindowManager {
        public WindowManagerImpl createLocalWindowManager(Window parentWindow) {
            return new WindowManagerImpl(mContext, parentWindow);
        }
    }
    //这里我们就知道a.getWindowManager()返回的就是WindowManagerImpl
    //及 wm.addView(decor, l);就是调用的WindowManagerImpl中的addView方法
    //WindowManagerImpl
    public final class WindowManagerImpl implements WindowManager {
        private final WindowManagerGlobal mGlobal = WindowManagerGlobal.getInstance();
        @Override
        public void addView(@NonNull View view, @NonNull ViewGroup.LayoutParams params) {
            applyDefaultToken(params);
            mGlobal.addView(view, params, mContext.getDisplay(), mParentWindow);
        }
    }
    //WindowManagerGlobal
    public final class WindowManagerGlobal {
        public void addView(View view, ViewGroup.LayoutParams params,
            Display display, Window parentWindow) {
         //省略部分代码

        ViewRootImpl root;
        View panelParentView = null;

        synchronized (mLock) {
            //省略部分代码
            root = new ViewRootImpl(view.getContext(), display);

            view.setLayoutParams(wparams);

            mViews.add(view);
            mRoots.add(root);
            mParams.add(wparams);

            // do this last because it fires off messages to start doing things
            try {
                root.setView(view, wparams, panelParentView);
            } catch (RuntimeException e) {
                // BadTokenException or InvalidDisplayException, clean up.
                if (index >= 0) {
                    removeViewLocked(index, true);
                }
                throw e;
            }
        }
      }
    }
    
    //调用ViewRootImpl的set方法
    public final class ViewRootImpl implements ViewParent,
        View.AttachInfo.Callbacks, ThreadedRenderer.DrawCallbacks {
        public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
        synchronized (this) {
            if (mView == null) {
                 //省略部分代码
                // Schedule the first layout -before- adding to the window
                // manager, to make sure we do the relayout before receiving
                // any other events from the system.
                requestLayout();
                 //省略部分代码
            }
        }
      }
         @Override
        public void requestLayout() {
            if (!mHandlingLayoutInLayoutRequest) {
                checkThread();
                mLayoutRequested = true;
                scheduleTraversals();
            }
        }
        final TraversalRunnable mTraversalRunnable = new TraversalRunnable();
        
        final class TraversalRunnable implements Runnable {
            @Override
            public void run() {
                doTraversal();
            }
        }
        
        void scheduleTraversals() {
            if (!mTraversalScheduled) {
            mTraversalScheduled = true;
            mTraversalBarrier = mHandler.getLooper().getQueue().postSyncBarrier();
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);
            if (!mUnbufferedInputDispatch) {
                scheduleConsumeBatchedInput();
            }
            notifyRendererOfFramePending();
            pokeDrawLockIfNeeded();
            }
        }
        
        void doTraversal() {
            if (mTraversalScheduled) {
                mTraversalScheduled = false;
                mHandler.getLooper().getQueue().removeSyncBarrier(mTraversalBarrier);

                if (mProfile) {
                    Debug.startMethodTracing("ViewAncestor");
                }

                performTraversals();

                if (mProfile) {
                    Debug.stopMethodTracing();
                    mProfile = false;
                }
            }
        }
        //绘制三大流程全在这里
        void performTraversals(){
             //省略部分代码
             performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
             //省略部分代码
             performLayout(lp, mWidth, mHeight);
             //省略部分代码
             performDraw();
             //省略部分代码
        }
        //measure
        private void performMeasure(int childWidthMeasureSpec, int childHeightMeasureSpec) {
            if (mView == null) {
                return;
            }
            Trace.traceBegin(Trace.TRACE_TAG_VIEW, "measure");
            try {
                mView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            } finally {
                Trace.traceEnd(Trace.TRACE_TAG_VIEW);
            }
        }
        
        //layout
        private void performLayout(WindowManager.LayoutParams lp, int desiredWindowWidth,
            int desiredWindowHeight) {
            final View host = mView;
            if (host == null) {
                return;
            }
            host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
        }
        //draw
        private void performDraw() {
            //省略部分代码
            boolean canUseAsync = draw(fullRedrawNeeded);
            //省略部分代码
        }
        private boolean draw(boolean fullRedrawNeeded) {
             //省略部分代码
             if (!drawSoftware(surface, mAttachInfo, xOffset, yOffset,
                        scalingRequired, dirty, surfaceInsets)) {
                    return false;
             }
        }
        //涉及到硬件或软件绘制
        private boolean drawSoftware(Surface surface, AttachInfo attachInfo, int xoff, int yoff,
            boolean scalingRequired, Rect dirty, Rect surfaceInsets) {
            //省略部分代码
             mView.draw(canvas);
             //省略部分代码
        }
    }
```
```java
   
```