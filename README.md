<p align="center">
  <a href="#">
    <img style="border-radius: 10px;width:100px" alt="Logo" src="./static-files/logo.png">
  </a>
</p>

<p align="center">
    <img src="https://img.shields.io/badge/-Java-D08031?logo=JAVA&logoColor=white" />
    <img src="https://img.shields.io/badge/-gralde-green" />
    <img src="https://img.shields.io/badge/-antlr-red?logo=antlr&logoColor=white" />
</p>

### 前言
> 哈喽,大家好 我是 Hor 👨🏻‍💻。


HorScript，它是一种脚本语言，旨在通过提供以**中文**来实现直观、灵活的语法来描述的小型编程语言 。


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
