#--------------DATABINDING-------------
 -dontwarn android.databinding.**
 -keep class android.databinding.** { *; }
-dontwarn android.databinding.tool.util.**

#---------------RXJAVA--------------

 -dontwarn sun.misc.**

 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 -keep class rx.schedulers.Schedulers {
     public static <methods>;
 }
 -keep class rx.schedulers.ImmediateScheduler {
     public <methods>;
 }
 -keep class rx.schedulers.TestScheduler {
     public <methods>;
 }
 -keep class rx.schedulers.Schedulers {
     public static ** test();
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     long producerNode;
     long consumerNode;
 }

-dontwarn javax.annotation.**
-dontwarn javax.inject.**

#------------RETROLAMBDA---------------

-dontwarn java.lang.invoke.*

