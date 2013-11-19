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
