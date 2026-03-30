import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class MeasurementApp extends JFrame {
    JPanel anaPanel;
    CardLayout kartlar;
    JLabel[] adimlar = new JLabel[5];
    String[] adimIsimleri = {"profile", "define", "plan", "collect", "analyse"};

    JTextField u, s, sn;
    JComboBox m;
    DefaultTableModel tm1, tm2;
    JPanel sonPanel;

    public MeasurementApp() {
        setTitle("ISO 15939");
        setSize(850, 650);
        setDefaultCloseOperation(3);

        JPanel ust = new JPanel();
        for(int i=0; i<5; i++) {
            adimlar[i] = new JLabel(adimIsimleri[i] + " ");
            ust.add(adimlar[i]);
        }
        add(ust, "North");

        kartlar = new CardLayout();
        anaPanel = new JPanel(kartlar);

        JPanel p1 = new JPanel();
        u = new JTextField(10); s = new JTextField(10); sn = new JTextField(10);
        p1.add(new JLabel("username:")); p1.add(u);
        p1.add(new JLabel("school:")); p1.add(s);
        p1.add(new JLabel("session name:")); p1.add(sn);
        JButton b1 = new JButton("next");
        b1.addActionListener(e -> {
            if(u.getText().equals("") || s.getText().equals("")) JOptionPane.showMessageDialog(null, "all fields must be filled in before proceeding to the next step.");
            else { goster(1); kartlar.show(anaPanel, "p2"); }
        });
        p1.add(b1);
        anaPanel.add(p1, "p1");

        JPanel p2 = new JPanel();
        JRadioButton r1 = new JRadioButton("product quality", true);
        JRadioButton r2 = new JRadioButton("process quality");
        ButtonGroup bg = new ButtonGroup(); bg.add(r1); bg.add(r2);
        m = new JComboBox(new String[]{"health", "education"});
        JButton b2 = new JButton("next");
        b2.addActionListener(e -> {
            tm1.addRow(new Object[]{"sus score", "50", "higher^", "0-100", "points"});
            tm1.addRow(new Object[]{"onboarding time", "50", "lower!", "0-60", "min"});
            goster(2); kartlar.show(anaPanel, "p3");
        });
        p2.add(r1); p2.add(r2); p2.add(m); p2.add(b2);
        anaPanel.add(p2, "p2");

        JPanel p3 = new JPanel(new BorderLayout());
        tm1 = new DefaultTableModel(new String[]{"metric", "coeff", "direction", "range", "unit"}, 0);
        p3.add(new JScrollPane(new JTable(tm1)));
        JButton b3 = new JButton("next");
        b3.addActionListener(e -> {
            tm2.addRow(new Object[]{"sus score", "higher^", "0-100", "89", "", "50/points"});
            tm2.addRow(new Object[]{"onboarding time", "lower!", "0-60", "5", "", "50/min"});
            goster(3); kartlar.show(anaPanel, "p4");
        });
        p3.add(b3, "South");
        anaPanel.add(p3, "p3");

        JPanel p4 = new JPanel(new BorderLayout());
        tm2 = new DefaultTableModel(new String[]{"metric", "direction", "range", "value", "score (1-5)", "coeff/unit"}, 0);
        p4.add(new JScrollPane(new JTable(tm2)));
        JButton b4 = new JButton("next");
        b4.addActionListener(e -> {
            for(int i=0; i<tm2.getRowCount(); i++) {
                double v = Double.parseDouble(tm2.getValueAt(i, 3).toString());
                String rng = tm2.getValueAt(i, 2).toString();
                double min = Double.parseDouble(rng.split("-")[0]);
                double max = Double.parseDouble(rng.split("-")[1]);
                double sc;
                if(tm2.getValueAt(i, 1).toString().contains("higher")) sc = 1 + ((v-min)/(max-min))*4;
                else sc = 5 - ((v-min)/(max-min))*4;
                tm2.setValueAt(Math.round(sc*2)/2.0, i, 4);
            }
            sonPanel.removeAll();
            double v1 = Double.parseDouble(tm2.getValueAt(0, 4).toString());
            double v2 = Double.parseDouble(tm2.getValueAt(1, 4).toString());
            double t = (v1+v2)/2.0;
            sonPanel.add(new JLabel("usability score: " + t));
            JProgressBar bar = new JProgressBar(0, 5); bar.setValue((int)t);
            sonPanel.add(bar);
            sonPanel.add(new JLabel("gap value: " + (5.0-t)));
            sonPanel.add(new JLabel("this dimension has the lowest score and requires the most improvement."));
            goster(4); kartlar.show(anaPanel, "p5");
        });
        p4.add(b4, "South");
        anaPanel.add(p4, "p4");

        sonPanel = new JPanel();
        sonPanel.setLayout(new BoxLayout(sonPanel, 1));
        anaPanel.add(sonPanel, "p5");

        add(anaPanel);
        goster(0);
        setVisible(true);
    }

    void goster(int x) {
        for(int i=0; i<5; i++) {
            if(i == x) adimlar[i].setForeground(Color.RED);
            else if(i < x) adimlar[i].setText(adimIsimleri[i] + " [v] ");
            else adimlar[i].setForeground(Color.BLACK);
        }
    }

    public static void main(String[] args) {
        new MeasurementApp();
    }
}
