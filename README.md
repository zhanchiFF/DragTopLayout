# DragTopLayout
refer to the chenupt's DragTopLayout &amp;&amp; hongyangAndroid's Android-StickyNavLayout，using ViewDragHelper class，considering the height of topview exceed the screen height

![image](https://github.com/zhanchiFF/DragTopLayout/raw/master/example.png)

# Base Useage
1、the layout must include two child views; and the topview(aboveview) has better to be common layout,such as LinearLayout,and can't sliding;It means ScrollView/ListView is not recommended;
2、the bottomView(belowview) can be any view; But,the manager that the bot 
tomview belong to (such as fragment,activity,or others) must implements the interface "DragTopNewGroup.BelowTouchListener";
