

## 前言

我们知道，Android  TextView 默认支持跑马灯效果，但是不够灵活，比如不能支持设置动画执行时长，动画效果等。


Github 上面也有一些优秀，实用的开源库。


作者 | 开源库 | star | 区别 | 实现原理
---|--- | --- | --- | ---
sunfusheng | [MarqueeView](https://github.com/sunfusheng/MarqueeView) | 2.5k + | 支持对 View 进行复用，仅支持 TextView（内部最多有三个 TextView） | 基于 ViewFilp 实现
gongwen | [MarqueeViewLibrary](https://github.com/gongwen/MarqueeViewLibrary) | 1.7k + | 不支持对 View 进行复用，支持各种 View | 基于 ViewFilp 实现



于是，我在想，能不能开发出一款支持对 View 进行复用，同时支持各种 View 的自定义控件出来了。终于，功夫不负有心人，最终实现了。支持的功能有

- 支持各种 View，通过 type 进行区分
- 内部对 View 进行复用，有多少种 type，内部就有多少个 View。
- 支持 view 的摆放位置（想做，向右，居中）
- 支持各种动画，从上而下，从左而右 等，设置动画时长
- 支持自定义动画
- 支持监听每一个 item 的点击事件
- 支持监听 flip 事件，即当前 flip 到哪个 item


## 效果图

我们先来看看效果图吧。

![](http://ww1.sinaimg.cn/large/9fe4afa0gy1fuxdtmpacvg20970ll186.gif)



## 使用说明

使用 MarqueeView 大概需要三个步骤：


### 第一步:在 Gradle 文件中配置：


```
implementation 'com.xj:marqueeview:<latest-version>'
```

目前最新的版本是 0.1.00，最新版本可以到该网址查看：[marqueeView]( https://bintray.com/xujun94/maven/marqueeView)

```
implementation 'com.xj:marqueeview:0.1.00'
```


### 第二步：在 XML 文件中使用


```
<com.xj.marqueeview.MarqueeView
    android:id="@+id/mv_multi_text5"
    android:layout_width="match_parent"
    android:layout_height="@dimen/mv_multi_text_height"
    android:layout_marginTop="10dp"
    android:background="@mipmap/bg"
    app:mvAnimDuration="500"
    app:mvDirection="top_to_bottom"
    app:mvInterval="3000">

</com.xj.marqueeview.MarqueeView>


```


自定义属性说明

属性| 说明
---|---
mvAnimDuration  | 动画执行时间
mvInterval	| View 翻页时间间隔
mvGravity	| View 的摆放位置left、center、right
mvDirection	| 动画滚动方向:bottom_to_top、top_to_bottom、right_to_left、left_to_right


### 第三步：给 MarqueeView 设置 Adapater

首先，若 MarqueeView 的 ViewType 只有一种类型，那么只需要继承 CommonAdapter 即可


```
public class SimpleTextAdapter extends CommonAdapter<String> {

    public SimpleTextAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_simple_text, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        TextView tv = viewHolder.getView(R.id.tv);
        tv.setText(item);
    }

}
```



```
SimpleTextAdapter simpleTextAdapter = new SimpleTextAdapter(mContext, datas);
simpleTextAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(int position, View view) {
        Log.i(TAG, "onItemClick: position = " + position);
        if (marqueeView.isStart()) {
            marqueeView.stopFilp();
        } else {
            marqueeView.startFlip();
        }
    }
});
marqueeView.setAdapter(simpleTextAdapter);


```

看一下效果图：

![](http://ww1.sinaimg.cn/large/9fe4afa0gy1fuxhq0rf8mg20970llaeu.gif)


---


## 更多用法

### 支持不同的 ViewType

![](http://ww1.sinaimg.cn/large/9fe4afa0gy1fuxhr1iguag20970llh7n.gif)


从 gif 效果图中，我们可以看到，一共有三种 type：

- 只含有 TextView
- 含有一个 ImageView 和 TextView
- 含有两个 TextView 和 一个ImageView

要想实现上述效果，需要两个步骤：


第一步：继承于 ItemViewDelegate，重写 getItemViewLayoutId，isForViewType，convert
 方法，其中 getItemViewLayoutId 方法表示返回布局 layoutId，convert 方法在刷新当前 View 的时候会调用，可以用来刷新数据


```
/**
 * Created by xujun on 1/9/2018$ 18:25$.
 */
public class TextItemViewDelegate implements ItemViewDelegate<MultiTypeBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_simple_text;
    }

    @Override
    public boolean isForViewType(MultiTypeBean item, int position) {
        return item.mItemViewType == MultiTypeBean.ItemViewType.text;
    }

    @Override
    public void convert(ViewHolder holder, MultiTypeBean multiTypeBean, int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setText(multiTypeBean.title);
    }


}
```

```
public class ImageTextItemViewDelegate implements ItemViewDelegate<MultiTypeBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_image_text;
    }

    @Override
    public boolean isForViewType(MultiTypeBean item, int position) {
        return item.mItemViewType == MultiTypeBean.ItemViewType.imageText;
    }

    @Override
    public void convert(ViewHolder holder, MultiTypeBean multiTypeBean, int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setText(multiTypeBean.title);

        ImageView iv = holder.getView(R.id.iv);
        iv.setImageResource(multiTypeBean.resImageId);
    }



}
```

```
public class MultiTextItemViewDelegate implements ItemViewDelegate<MultiTypeBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_multi_text;
    }

    @Override
    public boolean isForViewType(MultiTypeBean item, int position) {
        return item.mItemViewType == MultiTypeBean.ItemViewType.multiTextAndImage;
    }

    @Override
    public void convert(ViewHolder holder, MultiTypeBean multiTypeBean, int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setText(multiTypeBean.title);

        TextView tvContent = holder.getView(R.id.tv_content);
        tvContent.setText(multiTypeBean.content);

        ImageView iv = holder.getView(R.id.iv);
        iv.setImageResource(multiTypeBean.resImageId);
    }


}
```

第二步：将 ItemViewDelegate 添加到 MultiItemTypeAdapter 中，并给 marqueeView 设置 Adapter。


```
MultiItemTypeAdapter<MultiTypeBean> multiItemTypeAdapter = new MultiItemTypeAdapter<MultiTypeBean>(mContext, datas);
multiItemTypeAdapter.addItemViewDelegate(new TextItemViewDelegate());
multiItemTypeAdapter.addItemViewDelegate(new ImageTextItemViewDelegate());
multiItemTypeAdapter.addItemViewDelegate(new MultiTextItemViewDelegate());
multiItemTypeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(int position, View view) {
        Log.i(TAG, "onItemClick: position = " + position);
        if (marqueeView.isStart()) {
            marqueeView.stopFilp();
        } else {
            marqueeView.startFlip();
        }
    }
});
marqueeView.setAdapter(multiItemTypeAdapter);


```


### 其他用法

- 设置布局的对齐方向：

void setGravity(int gravity)

- 设置动画的方向：

void setDirection(int direction)

- 设置动画的执行时间：（内置动画支持，自定义动画不支持）

 void setAnimDuration(int animDuration)

- 设置两个 View 的轮播间隔

void setInterval(int interval)

- 设置进入进出动画（即自定义动画）

setInAndOutAnimation(Animation inAnimation, Animation outAnimation)

- 设置 Flip 监听

void setIFlipListener(IFlipListener IFlipListener)


```
mMvMultiText5.setIFlipListener(new MarqueeView.IFlipListener() {
    @Override
    public void onFilpStart(int position, View view) {
        Log.i(TAG, "onFilpStart: position = " + position);
    }

    @Override
    public void onFilpSelect(int position, View view) {
        Log.i(TAG, "onFilpSelect: position = " + position);
    }
});


```


- 设置点击事件监听


```
simpleTextAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(int position, View view) {
        Log.i(TAG, "onItemClick: position = " + position);
        if (marqueeView.isStart()) {
            marqueeView.stopFilp();
        } else {
            marqueeView.startFlip();
        }
    }
});
```

当然，以上功能也支持自定义属性：

自定义属性说明

属性| 说明
---|---
mvAnimDuration  | 动画执行时间
mvInterval	| View 翻页时间间隔
mvGravity	| View 的摆放位置left、center、right
mvDirection	| 动画滚动方向:bottom_to_top、top_to_bottom、right_to_left、left_to_right

----


## 感谢

https://github.com/hongyangAndroid/baseAdapter


参考了鸿洋大佬 baseAdapter 的大部分用法


https://github.com/sunfusheng/MarqueeView

里面 View 的复用也给了我相应的思路。不过 ViewFliper 无法实现多种 ViewType 的复用，最终舍弃了该方案，采用自定义 FrameLayout 的方式。



---


## 关于我

[GitHub: gdutxiaoxu](https://github.com/gdutxiaoxu)

[个人邮箱: gdutxiaoxu@136.com](gdutxiaoxu@136.com)


[CSDN 博客：https://blog.csdn.net/gdutxiaoxu](https://blog.csdn.net/gdutxiaoxu)

[简书主页: https://www.jianshu.com/u/ca9b3e19f454](https://www.jianshu.com/u/ca9b3e19f454)

个人微信公众号：

![](http://ww1.sinaimg.cn/large/9fe4afa0gy1fky6yqvcbbj209k09k748.jpg)

如果觉得效果还不错，请 star，谢谢。

[MarqueeView：https://github.com/gdutxiaoxu/MarqueeView](https://github.com/gdutxiaoxu/MarqueeView)

[Android 自定义 MarqueeView 实现跑马灯效果 - 使用说明](https://blog.csdn.net/gdutxiaoxu/article/details/82389133)



