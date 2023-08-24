package Experiment;

import java.util.*;

class process implements Comparable<process>{
    public String Name;//标识符
    public int InTime;//进入内存时间
    public int ServerTime;//服务时间
    public int priority;//优先级
    public int StartTime;//开始时间
    public int FinishTime;//完成时间
    public int TurnaroundTime=0;//周转时间
    public Double AddTurnaroundTime=0.0;//带权周转时间

    public int memory;

    public enum Status {
        RUNNING, SUSPENDED, TERMINATED
    }

    public boolean isActive = true;

    public Status status = Status.RUNNING;

    @Override
    public int compareTo(process o) {
        return this.InTime - o.InTime;
    }
}
public class Test {
    public static List<process> result=new ArrayList<>();//进程

    //创建
    public static void Input() {
        Scanner sc = new Scanner(System.in);

        process pro = new process();

        System.out.print("Enter Name: ");
        pro.Name = sc.next();

        System.out.print("Enter In Time: ");
        pro.InTime = sc.nextInt();

        System.out.print("Enter Server Time: ");
        pro.ServerTime = sc.nextInt();

        System.out.print("Enter Priority: ");
        pro.priority = sc.nextInt();

        System.out.print("Enter Memory: ");
        pro.memory = sc.nextInt();

        result.add(pro);
    }

    //挂起
    public static void SuspendProcess(String name) {
        for (process p : result) {
            if (p.Name.equals(name) && p.status != process.Status.TERMINATED) {
                p.status = process.Status.SUSPENDED;
                System.out.println("Process " + name + " suspended.");
                return;
            }
        }
        System.out.println("Process " + name + " not found or already terminated.");
    }

    //终止
    public static void TerminateProcess(String name) {
        for (process p : result) {
            if (p.Name.equals(name) && p.status != process.Status.TERMINATED) {
                p.status = process.Status.TERMINATED;
                System.out.println("Process " + name + " terminated.");
                return;
            }
        }
        System.out.println("Process " + name + " not found or already terminated.");
    }

    //激活
    public static void ActivateProcess(String name) {
        for (process p : result) {
            if (p.Name.equals(name) && p.status == process.Status.SUSPENDED && p.isActive) {
                p.status = process.Status.RUNNING;
                System.out.println("Process " + name + " activated.");
                return;
            }
        }
        System.out.println("Process " + name + " not found, not suspended, or not active.");
    }

    public static void PrintStatus(){
        System.out.println("进程标识符   进程状态    内存占用");
        for(process p:result){
            if(p.status == process.Status.RUNNING)
                System.out.println(p.Name+"                   "
                                   +p.status+"        "
                                   +p.memory+"MB");
            else {
                System.out.println(p.Name+"                   "
                        +p.status+"        "
                        +"0MB");
            }
        }
    }

    private static void PrintForm(){
        System.out.println("进程标识符  进入时间  服务时间  开始时间  完成时间  周转时间  带权值周转时间");
    }
    public  static void Print(process pro){

        System.out.println(pro.Name+"                   "
                +pro.InTime+"               "
                +pro.ServerTime+"               "
                +pro.StartTime+"                 "
                +pro.FinishTime+"                "
                +pro.TurnaroundTime+"                "
                +pro.AddTurnaroundTime);
    }
    public  static void FCFS(){
        //先来先服务
        System.out.println("批处理调度");
        if(result == null){
            throw new RuntimeException("请输入进程");
        }
        Queue<process> queue=new LinkedList<>();//内存队列
        Collections.sort(result);
        for(process newProcess:result){
            queue.offer(newProcess);
        }
        PrintForm();
        process tmp=queue.peek();//第一个输出的值
        if(tmp.status == process.Status.RUNNING)
        {
            tmp.StartTime=tmp.InTime;
            tmp.FinishTime=tmp.ServerTime+tmp.StartTime;
            tmp.TurnaroundTime=tmp.FinishTime-tmp.InTime;
            tmp.AddTurnaroundTime=(double)tmp.TurnaroundTime/(double) tmp.ServerTime;
            Print(tmp);
        }
        while (!queue.isEmpty()){
            process tmpe=queue.poll();
            if(queue.isEmpty()){
                break;
            }
            process top=queue.peek();
            if (top.status == process.Status.SUSPENDED || top.status == process.Status.TERMINATED) {
                continue; // Skip suspended or finished processes
            }
            if(tmpe.FinishTime>top.InTime){
                top.StartTime=tmpe.FinishTime;
            } else{
                top.StartTime=top.InTime;
            }
            top.FinishTime=top.StartTime+top.ServerTime;
            top.TurnaroundTime=top.FinishTime-top.InTime;
            top.AddTurnaroundTime=(double)top.TurnaroundTime/(double) top.ServerTime;
            Print(top);
        }
        AverNum();
    }


    public  static void SpfSort(int start, int end){ //根据服务时间排序
        for(int i = start; i < end; i++){
            for(int j = i + 1; j < end; j++){
                if(result.get(i).ServerTime> result.get(j).ServerTime){
                    process tem = result.get(i);
                    result.set(i,result.get(j));
                    result.set(j, tem);
                }
            }
        }
    }
    public  static void HpfSort(int start, int end, int temover){//根据优先级排序
        for(int i = start; i < end; i++){
            for(int j = i + 1; j < end; j++){
                int now =  (temover - result.get(i).InTime + result.get(i).ServerTime) / result.get(i).ServerTime;
                int next = (temover - result.get(j).InTime + result.get(j).ServerTime) / result.get(j).ServerTime;
                if(next > now){//值越大优先
                    process tem = result.get(i);
                    result.set(i,result.get(j));
                    result.set(j, tem);
                }
            }
        }
    }
    public  static int effective(int temover, int start){ //查看短进程有效值
        int end;  //去返回有效值的下标
        for(end = start; end < result.size(); end++){
            if(result.get(end).InTime > temover){
                break;
            }
        }
        return end;
    }
    public  static void AverNum(){
        if(result == null){
            throw new RuntimeException("请输入进程");
        }
        int Turnsum=0;
        double AddTurnsum=0.0;
        for(int i=0;i<result.size();i++){
            Turnsum+=result.get(i).TurnaroundTime;
            AddTurnsum+=result.get(i).AddTurnaroundTime;
        }
        double AverTurn=(double) Turnsum/result.size();
        double AverAddTurn=AddTurnsum/result.size();
        System.out.println("平均周转时间为:"+AverTurn);
        System.out.println("加权平均周转时间为:"+AverAddTurn);
    }
    public  static void SPF(){
        //短进程优先
        System.out.println("实时调度1");
        if(result == null){
            throw new RuntimeException("请输入进程");
        }
        Collections.sort(result);
        int temover = result.get(0).InTime;
        PrintForm();
        for (int i = 0; i < result.size(); i++) {

            int effect = effective(temover,i);  //查看进来了几个进程
            SpfSort(i,effect); //对进来的进程对短进程排序
            temover=InitOther(i,temover);
        }
        AverNum();//计算周转时间和带权周转时间
    }
    public  static void HPF() {
        //高优先级优先
        if (result == null) {
            throw new RuntimeException("请输入进程");
        }
        Collections.sort(result);
        System.out.println("实时调度2");
        PrintForm();
        int temover = result.get(0).InTime;
        for (int i = 0; i < result.size(); i++) {
            int effect = effective(temover, i);  //查看进来了几个进程
            HpfSort(i, effect, temover); //对进来的进程进行优先级排序
            temover=InitOther(i,temover);

        }
        AverNum();  //计算周转时间和带权周转时间
    }
    public  static void RR(int TimeSlice){
        //时间片轮转
        System.out.println("分时调度");
        if(result == null){
            throw new RuntimeException("请输入进程");
        }
        Collections.sort(result);
        Scanner scanner = new Scanner(System.in);
        int RR = TimeSlice;
        System.out.println("时间片大小："+RR);
        PrintForm();
        Queue<Integer> queue1=new LinkedList<>();
        int timeover = result.get(0).InTime;  //记录上个进程结束时间
        int[] serviceTem = new int[result.size()]; //存放所有的进程估计运行时间的,开始全为0.
        int i = 1; //看队列进了几个进程了
        queue1.offer(0); //排完序,肯定先执行第一个.

        while(!queue1.isEmpty() || i < result.size()){
            int cur = RR;
            if(queue1.isEmpty()) {
                for (int tep = 0; tep < result.size(); tep++) {
                    if (serviceTem[tep] == 0 && result.get(tep).status == process.Status.RUNNING) {
                        queue1.offer(tep);
                        i = i + 1;
                        timeover=result.get(tep).InTime;
                        break;
                    }
                }
            }
            //出队,进行执行
            int tem = queue1.poll();
            if(serviceTem[tem] == 0){  //当数组里估计运行时间为0的话,那就是第一次初始化,可以赋一下初始值.
                result.get(tem).StartTime = timeover;
            }
            while(cur != 0){   //模拟实现加时间片轮转,执行RR次,直到相等或用完。
                if(serviceTem[tem] != result.get(tem).ServerTime && result.get(tem).status == process.Status.RUNNING){
                    ++serviceTem[tem];
                    timeover++;
                }
                if(serviceTem[tem] == result.get(tem).ServerTime && result.get(tem).status == process.Status.RUNNING) {
                    result.get(tem).FinishTime =timeover;
                    result.get(tem).TurnaroundTime=result.get(tem).FinishTime-result.get(tem).InTime;
                    result.get(tem).AddTurnaroundTime=(double)result.get(tem).TurnaroundTime/result.get(tem).ServerTime;
                    Print(result.get(tem));
                    break;
                }
                cur--;
            }
            //i记录进程个数,去遍历所有进程,看还有那个没进入,如果进程到了,就插入队列.
            if(i < result.size()) {
                int j = i;
                for (; j < result.size(); j++) {
                    if (result.get(j).InTime <= timeover && result.get(tem).status == process.Status.RUNNING) {
                        queue1.offer(j);
                        i = i + 1;
                    }
                }
            }
            //如果当前进程没有执行完,就在进入队列.
            if(serviceTem[tem] != result.get(tem).ServerTime && result.get(tem).status == process.Status.RUNNING) {
                queue1.offer(tem);
            }
        }
        AverNum();
    }
    public  static int InitOther(int i,int temover){
        if(result.get(i).status == process.Status.RUNNING)
        {
            if (temover >= result.get(i).InTime) {
                result.get(i).StartTime = temover;
                result.get(i).FinishTime = result.get(i).StartTime + result.get(i).ServerTime;
                temover = result.get(i).FinishTime;
                result.get(i).TurnaroundTime=result.get(i).FinishTime-result.get(i).InTime;
                result.get(i).AddTurnaroundTime=(double)result.get(i).TurnaroundTime/result.get(i).ServerTime;
                Print(result.get(i));
                return temover;
            } else {
                result.get(i).StartTime = result.get(i).InTime;
                result.get(i).FinishTime = result.get(i).StartTime + result.get(i).ServerTime;
                temover = result.get(i).FinishTime; //记住他的结束时间.
                result.get(i).TurnaroundTime=result.get(i).FinishTime-result.get(i).InTime;
                result.get(i).AddTurnaroundTime=(double)result.get(i).TurnaroundTime/result.get(i).ServerTime;
                Print(result.get(i));
                return  temover;
            }
        }
        return temover;
    }
//    public static void main(String[] args) {
//        Scanner scan=new Scanner(System.in);
//        int choose=1;
//        while(choose !=0){
//            Menu();
//            choose=scan.nextInt();
//            if(choose == 0){
//                return;
//            } else if(choose==1){
//                Input();
//            } else if(choose == 2){
//                FCFS();
//            } else if(choose == 3){
//                SPF();
//            } else if(choose == 4){
//                HPF();
//            } else if(choose == 5){
//                RR();
//            } else{
//                System.out.println("输入错误");
//                continue;
//            }
//        }
//    }
}
