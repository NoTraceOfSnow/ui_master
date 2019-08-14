# 网易课堂第一节高级UI

#### 分析setContentView(R.layout.XXXX)加载过程

##### view是如何添加到屏幕上的？

##### 1.setContentView到底做了什么？

##### 2.我们设置的布局到底是怎么加载到屏幕上面的？

带着问题我们先来解决第一个问题。
```java
//    Activity
    public class Activity extends ContextThemeWrapper
        implements LayoutInflater.Factory2,
        Window.Callback, KeyEvent.Callback,
        OnCreateContextMenuListener, ComponentCallbacks2,
        Window.OnWindowDismissedCallback, WindowControllerCallback,
        AutofillManager.AutofillClient {
    public void setContentView(@LayoutRes int layoutResID) {
        //调用了window的setContentView方法
        getWindow().setContentView(layoutResID);
        initWindowDecorActionBar();
    }
    //返回了window对象
    public Window getWindow() {
        return mWindow;
    }
  }
```

从源码我们可以看到setContentView是调用了getWindow的setContentView。

```java
    /**
     * <p>The only existing implementation of this abstract class is
     * android.view.PhoneWindow, which you should instantiate when needing a
     * Window.
    */
    //从注释中我们知道PhoneWindow是Window的唯一实现类,那就是getWindow().setContentView(XXX)
    //其实调用的就是PhoneWindow的setContentView
    //Window
    public abstract class Window {}
    
    //PhoneWindow
    public class PhoneWindow extends Window implements MenuBuilder.Callback {
    @Override
    public void setContentView(int layoutResID) {
        // Note: FEATURE_CONTENT_TRANSITIONS may be set in the process of installing the window
        // decor, when theme attributes and the like are crystalized. Do not check the feature
        // before this happens.
        if (mContentParent == null) {
            /**
            *  做了哪些操作
            *  1.加载了DecorView
            *  2.DecorView加载一个基础布局xxx
            *  3.基础布局findById初始化mContentParent
            */
            installDecor();
        } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            mContentParent.removeAllViews();
        }

        if (hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            final Scene newScene = Scene.getSceneForLayout(mContentParent, layoutResID,
                    getContext());
            transitionTo(newScene);
        } else {
            //将我们需要显示的布局加载到mContentParent
            mLayoutInflater.inflate(layoutResID, mContentParent);
        }
        mContentParent.requestApplyInsets();
        final Callback cb = getCallback();
        if (cb != null && !isDestroyed()) {
            cb.onContentChanged();
        }
        mContentParentExplicitlySet = true;
    }
    
    private void installDecor() {
       mForceDecorInstall = false;
       if (mDecor == null) {
           //设置decorView
           mDecor = generateDecor(-1);
//           ...
       } else {
           mDecor.setWindow(this);
       }
       if (mContentParent == null) {
           //设置加载layout布局的父容器mContentParent
           mContentParent = generateLayout(mDecor);
//           ...
       }
//       ...
    }       
    //返回了一个DecorView对象
    protected DecorView generateDecor(int featureId) {
//        ...
        return new DecorView(context, featureId, this, getAttributes());
    }
    
    /**
     * The ID that the main layout in the XML layout file should have.
    */
    public static final int ID_ANDROID_CONTENT = com.android.internal.R.id.content;
    
    //返回了R.id.content的contentParent
    protected ViewGroup generateLayout(DecorView decor) {
//         ...
         //设置相关属性，一个layoutResource
         mDecor.onResourcesLoaded(mLayoutInflater, layoutResource);
         ViewGroup contentParent = (ViewGroup)findViewById(ID_ANDROID_CONTENT);
//         ...
         //返回了
         return contentParent;
    }
  }
```    
从源码上我们知道，setContentView初始化了一个DecorView,并且DecorView根据不同的属性初始化了一个原始布局ViewGroup。
ViewGroup肯定会有一个ID为com.android.internal.R.id.content的容器FrameLayout,然后将需要加载布局加载到FrameLayout上。  

布局加载流程
调用Activity.setContentView() -> 调用Window.setContentView -> 调用PhoneWindow.setContentView ->
初始化DecorView(加载基础容器) -> 初始化mContentParent父容器->加载需要显示的布局

[View绘制流程](ViewRead.md)
 
    
    
    
    