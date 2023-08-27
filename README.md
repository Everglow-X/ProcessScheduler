# ProcessScheduler
基于Swing的进程调度器，可用于操作系统课程设计
# 实现功能
1. 能实现模拟创建、撤销进程，实现挂起、激活等进程控制操作；
2. 输入进程信息，能够显示内存等资源总量变化情况；
3. 根据不同操作系统特性，如批处理、分时、实时等选用不同调度算法并显示进程运行情况，每种类型至少实现一种算法。
# 开发环境
IDEA+java19
# 界面展示
1. 创建进程
此功能可以模拟创建进程，输入进程信息，包括：进程名称，进程进入时间，进程服务时间，进程优先级，进程内存占用，并可在调度运行中随时创建新进程。
![image](https://github.com/Everglow-X/ProcessScheduler/assets/59141290/f7bb3de2-bdaf-438c-a126-07952ffac36c)
2. 挂起进程
此功能可以模拟挂起进程，将进程从内存换出至外存，在输入框中输入需要挂起的进程并确定，即可挂起进程。
![image](https://github.com/Everglow-X/ProcessScheduler/assets/59141290/1e5f4f7d-838d-495b-8bd2-78a475accd5e)
