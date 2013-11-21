Version 1.0.2 (2013-11-22)
-----------------------------

* [修复] #8 jetx 模板生成的 java 文件名可能会产生冲突
* [增强] #9 如果 compile.path 对应的目录非法或者没有权限不可写, 应该启动Engine时就报错
* [新增] #10 增加选项：compile.always 第一次访问模板强制编译
* [修复] #11 模板的路径如有使用 “../../file.jetx” 那么就会访问到 template.path 路径的外面
* [新增] #16 允许 import 一个单独的 Class, 避免出现冲突
* [新增] #17 增加 iterator(start,stop,step) 代替 range(…) 函数

Version 1.0.1 (2013-11-20)
-----------------------------

* [新增] #1 支持 Servlet 2.x
* [修复] #2 trim.directive.line 选项，如果指令两边为非空格，也会被 trim 
* [修复] #3 compile.debug 默认应该为 false
* [新增] #4 增加指令注释支持，如： <!-- #if (...) --> 增强对可视化编辑器的友好度
* [修复] #6 JDK 6 can't load the template class compiled using JDK 7.
* [修复] #7 JetTemplateServlet 和 JetTemplateFilter 默认可能输出错误的 contentType. 

Version 1.0.0 (2013-11-18)
-----------------------------

* 支持类似与 Velocity 的多种指令
* 支持静态编译
* 支持编译缓存
* 支持热加载
* 支持类型推导
* 支持泛型
* 支持可变参数方法调用
* 支持方法重载
* 支持类似于 Groovy 的方法扩展
* 支持函数扩展
* first public release
