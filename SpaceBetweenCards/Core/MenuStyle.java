package Core;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;

public class MenuStyle
{
	public static final Font DEFAULT_FONT = new Font("Arial", Font.BOLD, 20);
	
	public MenuStyle()
	{
		
	}
	
	public static void styleButton(JButton jButton)
	{
		jButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		jButton.setBackground(Color.BLUE);
		jButton.setForeground(Color.WHITE);
		jButton.setFont(DEFAULT_FONT);
	}
	
	public static void styleLabel(JLabel jLabel)
	{
		jLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		jLabel.setBackground(Color.BLACK);
		jLabel.setOpaque(true);
		jLabel.setForeground(new Color(150, 255, 150));
		jLabel.setFont(DEFAULT_FONT);
	}
}
