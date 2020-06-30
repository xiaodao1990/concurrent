<img src="G:\DevCache\Cache\concurrent-parent\Note\pic\015_concurrent.png" style="zoom:80%;" />

### 1、非公平锁实现原理

##### 加锁解锁流程

```java
1、先从构造器开始看，默认为非公平锁实现
ReentrantLock()
    -->Node head;
	-->Node tail;
	-->int state;
	-->Thread exclusiveOwnerThread;
2、加锁逻辑剖析
final void lock() {
	if (compareAndSetState(0, 1))
		setExclusiveOwnerThread(Thread.currentThread());
	else
		acquire(1);
}    
2.1、没有竞争，compareAndSetState(0, 1)将state由0改为1，修改成功。exclusiveOwnerThread设置为当前线程Thread-0。
```

<img src="G:\DevCache\Cache\concurrent-parent\Note\pic\016_concurrent.png" style="zoom:80%;" />

```java
2.2、第一次出现竞争
compareAndSetState(0, 1)=false
    acquire(1);
		tryAcquire(arg)// false
            NonfairSync.tryAcquire(int acquires)
            	nonfairTryAcquire(int acquires)
            		state=1
            		current == getExclusiveOwnerThread()//current=Thread-1,getExclusiveOwnerThread()=Thread-0 
            		return false;
        addWaiter(Node.EXCLUSIVE)
```

