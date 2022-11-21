<p align="center">
  <a href="#">
    <img style="border-radius: 10px;width:100px" alt="Logo" src="./static-files/logo.png">
  </a>
</p>

```
 _   _   _____   _____    _____   _____   _____    _   _____   _____  
| | | | /  _  \ |  _  \  /  ___/ /  ___| |  _  \  | | |  _  \ |_   _| 
| |_| | | | | | | |_| |  | |___  | |     | |_| |  | | | |_| |   | |   
|  _  | | | | | |  _  /  \___  \ | |     |  _  /  | | |  ___/   | |   
| | | | | |_| | | | \ \   ___| | | |___  | | \ \  | | | |       | |   
|_| |_| \_____/ |_|  \_\ /_____/ \_____| |_|  \_\ |_| |_|       |_|  
 　　　　
```
<p align="center">
    <img src="https://img.shields.io/badge/-Java-D08031?logo=JAVA&logoColor=white" />
    <img src="https://img.shields.io/badge/-gralde-green" />
    <img src="https://img.shields.io/badge/-antlr-red?logo=antlr&logoColor=white" />
</p>

### 前言
> 哈喽,大家好 我是 Hor 👨🏻‍💻。


HorScript，它是一种脚本语言，旨在通过提供以**中文**来实现直观、灵活的语法来描述的小型编程语言 。

这是一门用 JAVA 编写的一款弱类型的脚本语言，大部分语法参考了 Java 以及少量的 Python 和 JavaScript。

### 环境

- 开发工具： IDEA 
- JAVA： 1.8 版本
- 工具： Antlr
- 构建工具： Gradle 

## 目录结构

以下是系统的目录结构

```
├── static-files                 // 静态文件
├── src
│    ├── main                    
│    │    ├── antlr4             // antlr4 规则文件
│    │    ├── java               
│    │        ├── core           // 核心文件 由 antlr 生成
│    │        ├── domain         // Bean 封装
│    │        ├── exception      // 异常类
│    │        ├── parser         // 解析
│    │        ├── utils          // 工具
│    │        ├── HorScript.java // 提供运行方法
│    │        ├── main.java      // 测试入口
│    ├── test             
└──  build.gradle         // gradle 配置
```

## 特性
- [x] 弱类型定义: 语言中不会要求声明任何形式的类型结构
- [x] 数组类型
- [x] 特殊类型 `空`
- [x] 函数声明与调用
- [x] Lambda 函数
- [x] for, do while, while 循环
- [x] 具备简单逻辑处理能力：表达式计算、条件分支
- [x] 通用语法设计，学习成本极低

## 例子
### Hello world
```
打印("hello world");
```

## 文档
> 详情用法请看文档

- [HorScript文档](https://github.com/Hor-CN/HorScript/wiki)

## 联系作者
> hor@itbk.cn

![公众号](static-files/gzh.png)

## 致谢

- [antlr](https://github.com/antlr/antlr4)

