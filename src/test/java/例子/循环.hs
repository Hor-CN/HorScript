/** for...in 表达式
 *  用于循环访问可枚举集合（例如范围表达式、序列、列表、数组或其他支持枚举的构造）中的模式的匹配项
 */
打印换行("for...in 表达式: ")
赋值 list = [1,2,3,"四","五","六"]
循环(item 在 list) {
    打印(item," ")
}
打印换行()
/** for...to 表达式
 *  用于在一系列循环变量值中进行循环访问。
 */
打印换行("for...to 表达式: ")
循环(i = 1 到 10){
    打印(i," ")
}
打印换行()
/** while... 表达式
 *  判断循环用于在指定的测试条件为 真 时执行迭代操作（循环）
 */
打印换行("while... 表达式: ","i = ",i)
赋值 continueLooping = 真,conditions = 10,i = 0;
判断循环(continueLooping) {
    如果(i == conditions) {
        continueLooping = 假;
    }
    打印换行("i：",i)
    i = i + 1;
}
/** do ... while... 表达式
 *  判断循环用于在指定的测试条件为 真 时执行迭代操作（循环）
 */
打印换行("do ... while... 表达式: ","i = ",i)
赋值 continueLooping = 真,conditions = 10,i = 0;
开始 {
    如果(i == conditions) {
        continueLooping = 假;
    }
    打印换行("i：",i)
    i = i + 1;
}判断循环(continueLooping)
