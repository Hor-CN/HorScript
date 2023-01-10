/**
 * 测试作用域与声明提升
 * 函数⾸先会被提升，然后变量提升：函数声明优先级 > 变量声明优先级


// 打印换行("myFunction 函数外不能调用局部 car 变量:",car) // 将报空指针异常

myFunction() // 函数声明会自动提升到最顶层，所以可以在任意位置调用
函数 myFunction() {
    // 声明局部变量
    赋值 car = "宝马";
    // 函数内可调用 car 变量
    打印换行("myFunction 内可调用局部 car 变量:",car)
}

myFunction1() // 函数内声明的全局变量需要先调用函数使其生效
打印换行("myFunction1 函数外可以调用全局 car1：",car1)
函数 myFunction1() {
    // 声明该变量为全局变量，car1 在函数内，但是为全局变量。
    全局 car1 = "汗血宝马";
    // 函数内可调用 car1 变量
    打印换行("myFunction1 函数内可调用全局 car1 变量:",car1)
}

// 在全局作用域下使用全局关键字声明变量 = 使用赋值关键字 为全局变量 （全局 a = ？ == 赋值 a = ？）
打印换行("myFunction2 函数外可调用全局 car2 变量:",car2) // 变量声明会被提升
全局 car2 = "赤兔马";
函数 myFunction2() {
    打印换行("myFunction2 函数内可调用全局 car2 变量:",car2)
}
myFunction2()
 */

赋值 a = 123;
函数 x() {
  赋值 a = 456
  打印换行("in Function: ",a);
}
x()
打印换行(a)