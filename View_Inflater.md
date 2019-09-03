### View Inflater流程

LayoutInflater.inflate加载布局的时候子控件的layoutparam是怎么来的？
从源码看我们知道LayoutInflater加载过程中会调用根部局的generateLayoutParams
并且返回layoutparam，同时将layoutparam传递给子控件。

```java
 
 /**
 *  layoutInflater
 */
 public abstract class LayoutInflater {
        //调用inflate加载view
        public View inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
            return inflate(resource, root, root != null);
        }
        //调用xml解析器解析view
        public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {
            final Resources res = getContext().getResources();
            if (DEBUG) {
                Log.d(TAG, "INFLATING from resource: \"" + res.getResourceName(resource) + "\" ("
                    + Integer.toHexString(resource) + ")");
            }

            final XmlResourceParser parser = res.getLayout(resource);
            try {
                return inflate(parser, root, attachToRoot);
            } finally {
                parser.close();
            }
        }
        public View inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot) {
            synchronized (mConstructorArgs) {
                //省略部分代码
                // Temp is the root view that was found in the xml
                final View temp = createViewFromTag(root, name, inflaterContext, attrs);

                ViewGroup.LayoutParams params = null;
                if (root != null) { 
                    if (DEBUG) {
                        System.out.println("Creating params from root: " +
                                    root);
                    }
                    // Create layout params that match root, if supplied
                    //返回了viewGroup的param
                    params = root.generateLayoutParams(attrs);
                    if (!attachToRoot)
                        // Set the layout params for temp if we are not
                        // attaching. (If we are, we use addView, below)
                        temp.setLayoutParams(params);
                    }
                }

                if (DEBUG) {
                    System.out.println("-----> start inflating children");
                }
                //子控件也是使用ViewGroup中的layoutParam
                // Inflate all children under temp against its context.
                rInflateChildren(parser, temp, attrs, true);                
            }
            
    }
    /**
    * 调用ViewGroup  layoutParam 
    */
    public abstract class ViewGroup extends View implements ViewParent, ViewManager {
        public LayoutParams generateLayoutParams(AttributeSet attrs) {
            return new LayoutParams(getContext(), attrs);
        }
    }
```