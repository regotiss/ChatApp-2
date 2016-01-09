package source;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class MyRenderer extends DefaultListCellRenderer
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,boolean isSelected, boolean cellHasFocus) 
    {
        //JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value instanceof JLabel)
        {
        	 
             if (isSelected) {
                 setBackground(Color.yellow);
                 //setForeground(Color.blue);
                 setFont(new Font("Segoe Print", Font.BOLD|Font.ITALIC, 16));
             } else {
                 setBackground(Color.white);
                 //setForeground(Color.green);
                 setFont(new Font("Segoe Print", Font.BOLD, 20));
             }

            this.setText(((JLabel)value).getText());
            this.setIcon(((JLabel)value).getIcon());
            //this.setIcon(WindowUtil.getScaledImage(new ImageIcon(MessagePnl.class.getResource("/images/default1.png")).getImage(), 70, 70));
            
        }
        return this;
    }
}

