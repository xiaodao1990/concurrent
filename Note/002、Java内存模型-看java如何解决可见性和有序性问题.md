### Java内存模型

#### 什么是Java内存模型？

```java
导致可见性的原因是缓存，导致有序性的原因是编译优化，那么解决可见性、有序性问题最合理有效的方法就是"按需禁用缓存和编译优化"。
    
如何做到"按需禁用"呢？
    对于并发程序，所谓"按需禁用"其实就是俺早程序员的要求禁用。所以，为了解决可见性和有序性问题，只需要提供给程序员按需禁用缓存和编译优化的方法即可。
    
在程序员的视角来看，Java内存模型规范了如何提供按需禁用缓存和编译优化的方法。具体来说，这些方法包括volatile、synchronized 和 final 三个关键字，以及六项 Happens-Before 规则。
```

#### Happens-Before 规则

```java
参考源码
class VolatileExample {
  int x = 0;
  volatile boolean v = false;
  public void writer() {
    x = 42;
    v = true;
  }
  public void reader() {
    if (v == true) {
      // 这里x会是多少呢？
    }
  }
}
```



```java
Happens-Before 规则表达的是:"前面一个操作的结果对后续操作是可见的"。在现实世界中，如果A事件是导致B事件的起因，那么A事件一定是先于(Happens-Before)B事件的，这就是Happens-Before语义的现实理解。
    

和程序员有关的规则则一共有如下六项，都是关于可见性的。
	1）程序的顺序性规则
		这条规则是指在一个线程中，按照程序顺序，前面的操作Happens-Before于后续的任意操作。即程序前面对某个变量的修改一定是对后续操作可见的。
	2）volatile变量规则
		这条规则是指对一个volatile变量的写操作， Happens-Before 于后续对这个volatile变量的读操作。
	3）传递性
		这条规则是指如果A Happens-Before B，且B Happens-Before C，那么A Happens-Before C。
	从图中，我们可以这样理解：如果线程B读到了“v=true”，那么线程A设置的“x=42”对线程B是可见的。也就是说，线程B能看到 “x == 42”。
	这里包含了以下Happens-Before规则：
	1) “x=42” Happens-Before 写变量 “v=true” ，这是规则1的内容；
	2) 写变量“v=true” Happens-Before 读变量 “v=true”，这是规则2的内容。
	3) 根据传递性规则，线程B能看到 “x == 42”。
```

<img src="G:\DevCache\Cache\concurrent-parent\Note\pic\001_concurrent.png" alt="001_concurrent" style="zoom:80%;" />

```java
	4）管程中锁的规则
		这条规则是指对一个锁的解锁Happens-Before于后续对这个锁的加锁。
	要理解这个规则，就首先要了解“管程指的是什么”?
		管程是一种通用的同步原语，在Java中指的就是synchronized，synchronized是Java里对管程的实现。
	管程中的锁在Java里是隐式实现的，例如下面的代码，在进入同步块之前，会自动加锁，而在代码块执行完会自动释放锁，加锁以及释放锁都是编译器帮我们实现的。
	synchronized (this) { //此处自动加锁
      // x是共享变量,初始值=10
      if (this.x < 12) {
        this.x = 12; 
      }  
    } //此处自动解锁
    所以结合规则4——管程中锁的规则，可以这样理解：假设x的初始值是10，线程A执行完代码块后x的值会变成12（执行完自动释放锁），线程B进入代码块时，能够看到线程A对x的写操作，也就是线程B能够看到x==12。
        
	5）线程start()规则
        这条是关于线程启动的。它是指主线程A启动子线程B后，子线程B能够看到主线程在启动子线程B前的操作。具体可以参考下面示例代码。
	Thread B = new Thread(()->{
      // 主线程调用B.start()之前
      // 所有对共享变量的修改，此处皆可见
      // 此例中，var==77
    });
    // 此处对共享变量var修改
    var = 77;
    // 主线程启动子线程
    B.start();

	6）线程join()规则
		这条是关于线程等待的。如果在线程A中,调用线程B的join()并成功返回，那么线程B中的任意操作Happens-Before于该join()操作的返回。具体可参考下面示例代码。
	Thread B = new Thread(()->{
    	// 此处对共享变量var修改
    	var = 66;
    });
    // 例如此处对共享变量修改，
    // 则这个修改结果对线程B可见
    // 主线程启动子线程
    B.start();
    B.join()
    // 子线程所有对共享变量的修改
    // 在主线程调用B.join()之后皆可见
    // 此例中，var==66
```

#### 关键字理解

```
volatile
	它表达的是：告诉编译器，对这个变量的读写，不能使用CPU缓存，必须从内存中读取或者写入。
final
	final修饰变量时，初衷是告诉编译器：这个变量生而不变，可以可劲儿优化。
```

###### 课后作业分析

```
有一个共享变量 abc，在一个线程里设置了abc的值 abc=3，你思考一下，有哪些办法可以让其他线程能够看到abc==3？
1、共享变量abc,A线程中设置abc=3，A线程中start B线程，B线程可见abc=3.
2、共享变量abc,B线程中设置abc=3，A线程中join B线程，A线程中可见abc=3.
3、synchronized关键字对abc=3加锁，A线程先修改abc=3，B线程进来时，abc=3对B线程可见。
4、共享变量abc，A线程中abc = 3; volatile x = true;当B线程读取到x=true时，abc=3对B线程可见。
```

