package Experiment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ProcessSchedulerGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new MainFrame("Process Scheduler");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class MainFrame extends JFrame {
    private final JTextArea outputTextArea;

    public MainFrame(String title) {
        super(title);
        setLayout(new BorderLayout());

        outputTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        JButton inputButton = new JButton("创建进程");
        JButton suspendButton = new JButton("挂起进程");
        JButton terminateButton = new JButton("终止进程");
        JButton activateButton = new JButton("激活进程");
        JButton statusButton = new JButton("进程状态");
        JButton startFCFSButton = new JButton("批处理调度");
        JButton startSPFButton = new JButton("实时调度1");
        JButton startHPFButton = new JButton("实时调度2");
        JButton startRRButton = new JButton("分时调度");

        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInputDialog();
            }
        });

        startFCFSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeScheduler(Test::FCFS);
            }
        });

        startSPFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeScheduler(Test::SPF);
            }
        });

        startHPFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeScheduler(Test::HPF);
            }
        });

        startRRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int timeSlice = openTimeSliceDialog();
                if (timeSlice > 0) {
                    executeScheduler(() -> Test.RR(timeSlice));
                } else if (timeSlice == 0) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Time Slice cannot be zero.");
                }
            }
        });

        suspendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String processName = JOptionPane.showInputDialog(MainFrame.this, "Enter Process Name to Suspend:");
                if (processName != null) {
                    Test.SuspendProcess(processName);
                }
            }
        });

        terminateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String processName = JOptionPane.showInputDialog(MainFrame.this, "Enter Process Name to Terminate:");
                if (processName != null) {
                    Test.TerminateProcess(processName);
                }
            }
        });

        activateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String processName = JOptionPane.showInputDialog(MainFrame.this, "Enter Process Name to Activate:");
                if (processName != null) {
                    Test.ActivateProcess(processName);
                }
            }
        });

        statusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeScheduler(Test::PrintStatus);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(inputButton);
        buttonPanel.add(suspendButton);
        buttonPanel.add(terminateButton);
        buttonPanel.add(activateButton);
        buttonPanel.add(statusButton);
        buttonPanel.add(startFCFSButton);
        buttonPanel.add(startSPFButton);
        buttonPanel.add(startHPFButton);
        buttonPanel.add(startRRButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(800, 600);
    }

    private void openInputDialog() {
        JTextField nameField = new JTextField(10);
        JTextField inTimeField = new JTextField(5);
        JTextField serverTimeField = new JTextField(5);
        JTextField priorityField = new JTextField(5);
        JTextField memoryField = new JTextField(5);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("In Time:"));
        inputPanel.add(inTimeField);
        inputPanel.add(new JLabel("Server Time:"));
        inputPanel.add(serverTimeField);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityField);
        inputPanel.add(new JLabel("Memory(MB):"));
        inputPanel.add(memoryField);

        int result = JOptionPane.showConfirmDialog(
                this,
                inputPanel,
                "Enter Process Information",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            process newProcess = new process();
            newProcess.Name = nameField.getText();
            newProcess.InTime = Integer.parseInt(inTimeField.getText());
            newProcess.ServerTime = Integer.parseInt(serverTimeField.getText());
            newProcess.priority = Integer.parseInt(priorityField.getText());
            newProcess.memory = Integer.parseInt(memoryField.getText());
            Test.result.add(newProcess);
        }
    }

    private void executeScheduler(Runnable scheduler) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        scheduler.run();

        outputTextArea.setText(outputStream.toString());

        System.setOut(originalOut);
    }

    private int openTimeSliceDialog() {
        JTextField timeSliceField = new JTextField(5);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2));
        inputPanel.add(new JLabel("Time Slice:"));
        inputPanel.add(timeSliceField);

        int result = JOptionPane.showConfirmDialog(
                this,
                inputPanel,
                "Enter Time Slice",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            return Integer.parseInt(timeSliceField.getText());
        }
        return -1; // Return -1 to indicate no input or cancel
    }

}
