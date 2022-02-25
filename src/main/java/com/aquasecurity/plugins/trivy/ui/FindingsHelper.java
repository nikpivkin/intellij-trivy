package com.aquasecurity.plugins.trivy.ui;

import com.aquasecurity.plugins.trivy.model.Misconfiguration;
import com.aquasecurity.plugins.trivy.model.Vulnerability;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jdesktop.swingx.JXHyperlink;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.List;

public class FindingsHelper extends JPanel {

    private Misconfiguration misconfig;
    private String filepath;
    private Vulnerability vulnerability;

    public FindingsHelper() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(JBUI.Borders.empty(10));
    }



    private void updateHelp() {
        removeAll();
        if (this.misconfig == null && this.vulnerability == null) {
            return;
        }

        if (this.misconfig != null) {
            addHelpSection("", misconfig.description);
            addHelpSection("ID", misconfig.id);
            addHelpSection("Severity", misconfig.severity);
            addHelpSection("Resolution", misconfig.resolution);
            addHelpSection("Filename", filepath);

            if (misconfig.references.size() > 0) {
                addLinkSection(misconfig.references);
            }
        }

        if (this.vulnerability != null) {
            addHelpSection("", vulnerability.vulnerabilityID);
            addHelpSection(vulnerability.title, vulnerability.description );
            addHelpSection("Severity", vulnerability.severity);
            addHelpSection("Package Name", vulnerability.pkgName);
            addHelpSection("Installed Version", vulnerability.installedVersion);
            addHelpSection("Fixed Version", vulnerability.fixedVersion);
            addHelpSection("Filename", this.filepath);


            if (vulnerability.references.size() > 0) {
                addLinkSection(vulnerability.references);
            }
        }
    }

    private void addLinkSection(List<String> links) {
        JPanel section = new JPanel();
        section.setBorder(JBUI.Borders.empty(10));

        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));

        Font font = UIUtil.getLabelFont();
        Font headingFont = font.deriveFont(font.getStyle() | Font.BOLD);
        JLabel heading = new JLabel();
        heading.setFont(headingFont);
        heading.setText("Links");
        section.add(heading);

        links.forEach(link -> {
            JXHyperlink hyperlink = new JXHyperlink();
            hyperlink.setText(String.format("<html>%s</html>", link));
            hyperlink.setURI(URI.create(link));
            hyperlink.setToolTipText(link);
            hyperlink.setClickedColor(hyperlink.getUnclickedColor());
            hyperlink.setBorder(JBUI.Borders.emptyTop(5));
            section.add(hyperlink);

        });
        add(section);
    }

    private void addHelpSection(String title, String content) {
        JPanel section = new JPanel();
        section.setBorder(JBUI.Borders.empty(10));

        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));

        Font font = UIUtil.getLabelFont();
        Font headingFont = font.deriveFont(font.getStyle() | Font.BOLD);
        if (!title.isEmpty()) {
            JLabel heading = new JLabel();
            heading.setFont(headingFont);
            heading.setText(String.format("<html>%s</html>", title));
            section.add(heading);
        }

        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setFont(title.isEmpty() ? headingFont : font);
        descriptionLabel.setText(String.format("<html>%s</html>", content));
        descriptionLabel.setBorder(JBUI.Borders.emptyTop(5));
        section.add(descriptionLabel);
        add(section);
    }

    public void setMisconfiguration(Misconfiguration misconfig, String filepath) {
        this.vulnerability = null;
        this.misconfig = misconfig;
        this.filepath = filepath;
        updateHelp();
        this.validate();
        this.repaint();
    }

    public void setVulnerability(Vulnerability vulnerability, String filename) {
        this.misconfig = null;
        this.vulnerability = vulnerability;
        this.filepath = filepath;
        updateHelp();
        this.validate();
        this.repaint();
    }
}


