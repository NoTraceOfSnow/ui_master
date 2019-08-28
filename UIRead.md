#### UI绘制详细步骤流程

![UI绘制详细步骤流程](app\src\main\res\mipmap-xhdpi\ui.png)


从UI的绘制流程中我们知道，布局加载到界面会调用view的measure，layout，draw方法。

我们先要了解一个类MeasureSpec
我们知道view测量需要测量两部分，及View=模式+尺寸 
MeasureSpec就是View的模式和尺寸的封装类。
MeasureSpec是一个32位的int值
00000000000000000000000000000000
前两位表示的是view的模式
后三十位表示的是view的尺寸
```java
 public static class MeasureSpec {
        private static final int MODE_SHIFT = 30;
        private static final int MODE_MASK  = 0x3 << MODE_SHIFT;
        //00000000000000000000000000000000
        //对应表示不对view做任何限制，一般系统使用
        public static final int UNSPECIFIED = 0 << MODE_SHIFT;
        //01000000000000000000000000000000
        //父容器检测view大小，view的大小就是specSize，对应的layoutparams match_parent
        public static final int EXACTLY     = 1 << MODE_SHIFT;
        //10000000000000000000000000000000
        //指定一个可用大小，view不能超过这个值对应的layoutparam warp_content
        public static final int AT_MOST     = 2 << MODE_SHIFT;
        
        public static int makeMeasureSpec(@IntRange(from = 0, to = (1 << MeasureSpec.MODE_SHIFT) - 1) int size,
                                          @MeasureSpecMode int mode) {
            if (sUseBrokenMakeMeasureSpec) {
                return size + mode;
            } else {
                return (size & ~MODE_MASK) | (mode & MODE_MASK);
            }
        }
    }
```
###### ViewRootImpl 
源码上分析我们知道一个view添加到界面是需要过ViewRootImpl的performTraversals方法的

```java
public final class ViewRootImpl implements ViewParent,
        View.AttachInfo.Callbacks, ThreadedRenderer.DrawCallbacks {
        private void performTraversals() {
             //...省略部分代码
             int childWidthMeasureSpec = getRootMeasureSpec(mWidth, lp.width);
             int childHeightMeasureSpec = getRootMeasureSpec(mHeight, lp.height);
             performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
             //...省略部分代码
             performLayout(lp, mWidth, mHeight);
             //...省略部分代码
             performDraw();
             //...省略部分代码
        } 
        private static int getRootMeasureSpec(int windowSize, int rootDimension) {
            int measureSpec;
            switch (rootDimension) {
                case ViewGroup.LayoutParams.MATCH_PARENT:
                    // Window can't resize. Force root view to be windowSize.
                measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.EXACTLY);
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                // Window can resize. Set max size for root view.
                measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.AT_MOST);
                break;
            default:
                // Window wants to be an exact size. Force root view to be that size.
                measureSpec = MeasureSpec.makeMeasureSpec(rootDimension, MeasureSpec.EXACTLY);
                break;
            }
            return measureSpec;
        }
    }
```

### View measure

```java
//view  measure
    public class View implements Drawable.Callback, KeyEvent.Callback,
        AccessibilityEventSource {
    
      public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean optical = isLayoutModeOptical(this);
        if (optical != isLayoutModeOptical(mParent)) {
            Insets insets = getOpticalInsets();
            int oWidth  = insets.left + insets.right;
            int oHeight = insets.top  + insets.bottom;
            widthMeasureSpec  = MeasureSpec.adjust(widthMeasureSpec,  optical ? -oWidth  : oWidth);
            heightMeasureSpec = MeasureSpec.adjust(heightMeasureSpec, optical ? -oHeight : oHeight);
        }

        // Suppress sign extension for the low bytes
        long key = (long) widthMeasureSpec << 32 | (long) heightMeasureSpec & 0xffffffffL;
        if (mMeasureCache == null) mMeasureCache = new LongSparseLongArray(2);

        final boolean forceLayout = (mPrivateFlags & PFLAG_FORCE_LAYOUT) == PFLAG_FORCE_LAYOUT;

        // Optimize layout by avoiding an extra EXACTLY pass when the view is
        // already measured as the correct size. In API 23 and below, this
        // extra pass is required to make LinearLayout re-distribute weight.
        final boolean specChanged = widthMeasureSpec != mOldWidthMeasureSpec
                || heightMeasureSpec != mOldHeightMeasureSpec;
        final boolean isSpecExactly = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY;
        final boolean matchesSpecSize = getMeasuredWidth() == MeasureSpec.getSize(widthMeasureSpec)
                && getMeasuredHeight() == MeasureSpec.getSize(heightMeasureSpec);
        final boolean needsLayout = specChanged
                && (sAlwaysRemeasureExactly || !isSpecExactly || !matchesSpecSize);
        //当有缓存
        if (forceLayout || needsLayout) {
            // first clears the measured dimension flag
            mPrivateFlags &= ~PFLAG_MEASURED_DIMENSION_SET;

            resolveRtlPropertiesIfNeeded();

            int cacheIndex = forceLayout ? -1 : mMeasureCache.indexOfKey(key);
            if (cacheIndex < 0 || sIgnoreMeasureCache) {
                // measure ourselves, this should set the measured dimension flag back
                onMeasure(widthMeasureSpec, heightMeasureSpec);
                mPrivateFlags3 &= ~PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;
            } else {
                long value = mMeasureCache.valueAt(cacheIndex);
                // Casting a long to int drops the high 32 bits, no mask needed
                setMeasuredDimensionRaw((int) (value >> 32), (int) value);
                mPrivateFlags3 |= PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;
            }

            // flag not set, setMeasuredDimension() was not invoked, we raise
            // an exception to warn the developer
            if ((mPrivateFlags & PFLAG_MEASURED_DIMENSION_SET) != PFLAG_MEASURED_DIMENSION_SET) {
                throw new IllegalStateException("View with id " + getId() + ": "
                        + getClass().getName() + "#onMeasure() did not set the"
                        + " measured dimension by calling"
                        + " setMeasuredDimension()");
            }

            mPrivateFlags |= PFLAG_LAYOUT_REQUIRED;
        }

        mOldWidthMeasureSpec = widthMeasureSpec;
        mOldHeightMeasureSpec = heightMeasureSpec;
//      当没有缓存重新赋值
        mMeasureCache.put(key, ((long) mMeasuredWidth) << 32 |
                (long) mMeasuredHeight & 0xffffffffL); // suppress sign extension
        }
        
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        }
        
        protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
            boolean optical = isLayoutModeOptical(this);
            if (optical != isLayoutModeOptical(mParent)) {
                Insets insets = getOpticalInsets();
                int opticalWidth  = insets.left + insets.right;
                int opticalHeight = insets.top  + insets.bottom;
                measuredWidth  += optical ? opticalWidth  : -opticalWidth;
                measuredHeight += optical ? opticalHeight : -opticalHeight;
            }
            setMeasuredDimensionRaw(measuredWidth, measuredHeight);
        }
        
        private void setMeasuredDimensionRaw(int measuredWidth, int measuredHeight) {
            mMeasuredWidth = measuredWidth;
            mMeasuredHeight = measuredHeight;
            mPrivateFlags |= PFLAG_MEASURED_DIMENSION_SET;
        }
    }
```
从源码上我们知道View调用了onMeasure，从源码分析DecorView是FrameLayout及View调用的是FrameLayout的onMeasure方法
```java
//frameLayout
public class FrameLayout extends ViewGroup {
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //遍历子view
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (mMeasureAllChildren || child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                //省略代码
            }
            //省略代码
        }
    }
}
```
调用ViewGroup的measureChildWithMargins
```java
public abstract class ViewGroup extends View implements ViewParent, ViewManager {
    protected void measureChildWithMargins(View child,
            int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
    
    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
        // Parent has imposed an exact size on us
        case MeasureSpec.EXACTLY:
            if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size. So be it.
                resultSize = size;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size. It can't be
                // bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

        // Parent has imposed a maximum size on us
        case MeasureSpec.AT_MOST:
            if (childDimension >= 0) {
                // Child wants a specific size... so be it
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size, but our size is not fixed.
                // Constrain child to not be bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size. It can't be
                // bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

        // Parent asked to see how big we want to be
        case MeasureSpec.UNSPECIFIED:
            if (childDimension >= 0) {
                // Child wants a specific size... let him have it
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size... find out how big it should
                // be
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = MeasureSpec.UNSPECIFIED;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size.... find out how
                // big it should be
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = MeasureSpec.UNSPECIFIED;
            }
            break;
        }
        //noinspection ResourceType
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    } 
}
```
#### view的测量-确定view的measureSpec

![measureSpec测量](app\src\main\res\mipmap-xhdpi\measure_spec.png)

#### 控件测量过程

ViewGroup measure -> onMeasure(测量子控件的宽高) -> setMeasuredDimension -> setMeasuredDimensionRaw(保存自己的宽高)
View measure -> onMeasure -> setMeasuredDimension -> setMeasuredDimensionRaw(保存自己的宽高)

### view layout
View 调用view.layout确定自身位置，即确定mLeft,mRight,mTop,mBottom

viewGroup 调用view.layout确定自身位置 -> onLayout 确定子view的位置

```java
public class View implements Drawable.Callback, KeyEvent.Callback,
        AccessibilityEventSource {
        public void layout(int l, int t, int r, int b) {
            //省略代码
            int oldL = mLeft;
            int oldT = mTop;
            int oldB = mBottom;
            int oldR = mRight;
            //省略代码
        }
    }
```

### view Draw

draw绘制流程
viewGroup
1.绘制背景
2.如果需要进行图层的保存
3.绘制自己的内容
4.绘制子view的内容
5.绘制装饰(滚动条)

view
1.绘制背景
2.如果需要进行图层的保存
3.绘制自己的内容
5.绘制装饰(滚动条)


```java
public final class ViewRootImpl implements ViewParent,
        View.AttachInfo.Callbacks, ThreadedRenderer.DrawCallbacks {
        private void performTraversals() {
             //...省略部分代码
             performDraw();
             //...省略部分代码
        } 
        private void performDraw() {
             //...省略部分代码
            boolean canUseAsync = draw(fullRedrawNeeded);
            //...省略部分代码
        }
        private boolean draw(boolean fullRedrawNeeded) {
             //...省略部分代码
             if (!drawSoftware(surface, mAttachInfo, xOffset, yOffset,
                        scalingRequired, dirty, surfaceInsets)) {
                    return false;
             }
             //...省略部分代码
        }
        private boolean drawSoftware(Surface surface, AttachInfo attachInfo, int xoff, int yoff,
            boolean scalingRequired, Rect dirty, Rect surfaceInsets) {
            //...省略部分代码
            //调用decorView的draw方法
            mView.draw(canvas);
            //...省略部分代码
        }
   }
   public class View implements Drawable.Callback, KeyEvent.Callback,
        AccessibilityEventSource {
    public void draw(Canvas canvas) {
     /*
      * Draw traversal performs several drawing steps which must be executed
      * in the appropriate order:
      *
      *      1. Draw the background
      *      2. If necessary, save the canvas' layers to prepare for fading
      *      3. Draw view's content
      *      4. Draw children
      *      5. If necessary, draw the fading edges and restore layers
      *      6. Draw decorations (scrollbars for instance)
      */
    }
   }
```
