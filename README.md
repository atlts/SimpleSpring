IOC

1.利用loadBeanDefinayion读取xml文件，就是根据element把xml文件拆开再把属性直接存进BeanDefination的map里面，省去初始化的时间

2.registerBeanDefinition将beanDefination的信息放入xmlBeanFactory类的beanDefinitionMap里面

3.registerBaenPostProcessor基本上是和AOP相关的东西

4.通过getBean获取bean，没有初始化的bean进行初始化。其中initializeBean也基本适合AOP相关的东西。再把初始化的bean放入beanDefinition中，此处便是单例模式

没有做到的事：没有实现InitializeBean方法，只实现了一个Aware接口，没有做到避免循环引用，只有一个伪单例模式，也没有实现Destruction接口来回收bean

AOP


1.将切面的目标对象，切面，方法拦截器都作为类注册进入xml文件，切面类（AspectJAwareAdvisorAutoProxyCreator ）中会存储方法拦截器和切点表达式

2.当通过XmlFactory调用loadBeanDefinitions方法解析xml文件时，会和IOC一样调用XmlBeanReader类。但是在registerBeanPostProcessor()会将实现了BeanPostProcessor的bean，也就是AspectJAwareAdvisorAutoProxyCreator 类的bean全部存储起来，为之后利用这些bean进行后处理，比如生成代理对象，做准备。

3.当切面类对象通过getBean方法获得目标对象时，通过createBean（）方法在对bean进行初始化赋值时会有不一样。此时第2步存储的bean会获得相应的xmlBeanFactory

4.所有对象通过initializeBean（）方法时都会调用第二步AspectJAwareAdvisorAutoProxyCreator 类的bean的
postProcessAfterInitialization()方法，此时切面类会用自己的切点表达式和对应的bean对象进行匹配，匹配成功就会获取对应的bean的代理。此处是代理模式


此Spring的bean实例化过程

实例化bean对象 ---> 设置对象属性 ---> 检查Aware相关接口并设置依赖 ---> BeanPostProcessor的前置后置处理 --->使用
