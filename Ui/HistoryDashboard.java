import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HistoryDashboard extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public HistoryDashboard() {
        setLayout(new BorderLayout());

        // Top Panel with Image Header
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(10, 200));
        panel.setLayout(new BorderLayout(0, 0));

        JLabel lblNewLabel_1 = new JLabel("");
        ImageIcon HeaderImg = new ImageIcon(getClass().getResource("libraryHeading.png"));
        Image HeaderImg2 = HeaderImg.getImage().getScaledInstance(1080, 500, Image.SCALE_SMOOTH);
        lblNewLabel_1.setIcon(new ImageIcon(HeaderImg2));
        panel.add(lblNewLabel_1, BorderLayout.CENTER);

        add(panel, BorderLayout.NORTH);
	}

}
