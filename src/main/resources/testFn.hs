/*
赋值 lambda = () => {
    打印('123465')
}
函数 list(x) {
    如果(x > 2) {
        返回 "123";
    }
    返回 list(3)
}
打印(list(1))
函数 a() {
    返回 () => {
        打印(123)
    }
}

赋值 x = a()
x()


// 打印杨辉三角
函数 triangle(row) {
    赋值 list = [];
    // 每行元素个数跟行数一致
    循环(i=1 到 row) {
        list = list + [0]*i
    }
    // 边界赋值
    循环(i=0 到 row - 1) {
        list[i][0] = 1; // 左边界
        list[i][i] = 1; // 右边界（对角线）
    }
    // 内部元素采用递推公式计算
    循环(i=2 到 row - 1) {
        赋值 j = 1
        判断循环(j < i) {
            list[i][j] = list[i-1][j-1] + list[i-1][j]
            j = j + 1
        }
    }
    返回 list
}
函数 main(row) {
     // 输出杨辉三角形
     赋值 list = triangle(row);
     循环(i=0 到 row - 1) {
        循环(j=0 到 (row-i-1)/2) {
            打印(" ")
        }
        循环(j=0 到 i) {
            打印(list[i][j]," ")
        }
        打印换行();
     }
}
main(7)
*/




// 杨辉三角形
赋值 rows = 10
循环(i = 0 到 rows) {
    赋值 number = 1
    // 打印空格字符串
    打印(" "*(rows - i) * 2)
    循环(j = 0 到 i) {
        如果(number >= 100) {
            打印(" ",number)
        }否则 如果(number >= 10){
            打印("  ",number)
        }否则{
            打印("   ",number)
        }
        number = number * (i - j) / (j + 1);
    }
    打印换行();
}