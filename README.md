## VarietyTabLayout

一个可扩展的 `TabLayout`，目前支持：

- 自定义 `Tab` 样式，但是最外层的 `ViewGroup` 暂不支持 `Margin` 属性
- 自定义 `Indicator` 样式
  <br/>
  同时 `VarietyTabLayout 内置了几种简单的`Tab`布局和`Indicator` 样式
- `Tab` 样式
  - `TrackTabDecorator` : 文字颜色变化效果
  - `ScaleTabDecorator` : 文字缩放效果
- `Indicator` 样式
  - `FixedIndicator` : 固定长度指示器
  - `DynamicIndicator` : 动态框
  - `NylonIndicator` : 尼龙绳效果指示器

具体效果`clone`代码查看或者下载 [sample.apk](https://github.com/ytempest/VarietyTabLayout/blob/master/sample.apk) 体验一下



<br/>

### 依赖配置

`VarietyTabLayout` 的使用依赖 `ViewPager` ，所以也需要加上 `ViewPager` 的依赖

```groovy
implementation 'com.ytempest:varietytablayout:1.0'
```



<br/>

### 简单使用

在布局中添加 `VarietyTabLayout` 后，关联 `ViewPager` 后设置相应的 `Adapter`

```java
mVarietyTabLayout.setupWithViewPager(mViewPager);
List<String> items = getItems();
mViewPager.setAdapter(new Adapter(items))
mVarietyTabLayout.setAdapter(new SimpleAdapter(items))
```



<br/>

### 扩展——自定义样式

1. 自定义 `Tab` 样式，可参考内置的 ``ScaleTabDecorator` ，通过实现 `ITabDecorator<Data>` 接口实现 `Tab` 的处理逻辑：

   1. 创建 `Tab` 布局
   2. 将关联的`<Data>`类型的数据绑定到 `Tab` 布局中
   3. 根据百分比为当前 `Tab` 实现释放动画
   4. 根据百分比为下一个 `Tab` 实现选中动画

   

2. 自定义 `Indiator` 样式，可参考内置的 `FixedIndicator` ，通过实现 `IIndicatorDecorator` 接口实现 `Indicator` 的逻辑

   1. 设置 `Indicator` 在布局的位置
   2. 创建 `Indicator` 
   3. 计算 `Indicator` 的偏移位置

最后通过 `BaseAdapter` 选择自定义的样式



<br/>

### 预览图

这里为内置的 `Tab` 和 `Indicator` 样式

<br/>

![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d93098e1a5994bf2a8ebda556577bb33~tplv-k3u1fbpfcp-watermark.image)<

<br/>