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

// 打印杨辉三角
函数 num(x,y) {
    如果(y == 1 || y == x) {
        返回 1;
    }
    赋值 v1 = num(x - 1, y - 1);
    赋值 v2 = num(x - 1, y);
    赋值 v = v1 + v2;
    返回 v;
}
函数 main(row) {
    赋值 i = 1;
    判断循环(i <= row) {
        赋值 j = 1;
        赋值 x = 1;
        判断循环(j <= (row - i)) {
            打印(' ');
            j = j + 1;
        }
        判断循环(x <= i) {
            打印(num(i,j)," ")
            x = x + 1;
        }
        打印换行();
        i = i + 1;
    }
}

main(7)
*/
函数 a() {
    返回 () => {
        打印(123)
    }
}

赋值 x = a()
x()