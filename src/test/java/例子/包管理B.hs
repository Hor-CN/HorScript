/** 导入功能到你的脚本
* 你想在模块外面使用一些功能，那你就需要导入他们并设置别名才能使用。
*/

导入 './包管理A.hs' 为 测试; // 导入 包管理A.hs 的内容 设置别名为 测试

// 调用导入的方法
打印("调用A中的方法：")
测试.你好世界()

打印换行()

打印("调用A中的变量：")
打印换行(测试.变量)

打印("调用A中的对象：")
打印换行(测试.对象)
打印("调用A中的对象技能：")
测试.对象.技能()
