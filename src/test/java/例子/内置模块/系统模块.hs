导入 @'系统模块' 为 系统;

打印换行("---系统模块---")
打印换行(系统)
打印换行("------------")
打印换行("HorScript版本:",系统.版本)
打印换行("当前时间戳:",系统.当前时间戳())

打印("请输入您的姓名：")
赋值 name = 系统.输入()
打印("请输入您的分数：")
赋值 score = 系统.输入数字()
打印("请输入您的专业：")
赋值 major = 系统.输入()
打印换行("尊敬的",name,"同学,您的",major,"成绩",score <60 ? "不合格,请重修": "合格：" + score + "分")

