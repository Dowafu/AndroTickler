package base;

import cliGui.OutBut;
import initialization.TicklerVars;
import base.FileUtil;

/* 
 * Wrapper for cliGui.OutBut and HTML writer format
 */
public class OutWriter {
    private boolean isHtml;
    private FileUtil squeezeFileUtil;

    public OutWriter (boolean isHtml) {
        this.isHtml = isHtml;
        if (this.isHtml) {
            this.squeezeFileUtil = new FileUtil();
            if (this.squeezeFileUtil != null) {
                this.squeezeFileUtil.writeFile(TicklerVars.squeezeFile+".html", "");
                this.setHtmlHead();
                this.setBody();
                this.writeSqueezeFile("<h1> Code Squeeze HTML </h1>");
                this.setToC();
            }  
        }
    }

    private void writeSqueezeFile(String line) {
        if (this.squeezeFileUtil != null) {
            this.squeezeFileUtil.appendToFile(TicklerVars.squeezeFile+".html", line + "\n");
        }   
    }

    private void setHtmlHead() {
        String style = "<html>\n"+
            "<head>\n" +
            "<style>\n" +
            "pre {white-space: pre-wrap;    }\n" +
            "</style>\n" +
            "</head>\n";

        this.writeSqueezeFile(style);
        
    }

    private void setToC() {
        String toc = "\n<div id=\"toc\"></div>\n" + 
            "<div id=\"content\">\n" +
            "<script src=\"https://cdn.jsdelivr.net/npm/tocbot/dist/tocbot.min.js\"></script>\n" + 
            "<script>\n" + //
            "document.addEventListener(\"DOMContentLoaded\", function () {\n" + //
            "  tocbot.init({\n" + //
            "    tocSelector: '#toc',\n" + //
            "    contentSelector: '#content',\n" + //
            "    headingSelector: 'h1, h2, h3',\n" + //
            "    collapseDepth: 6\n" + //
            "  });\n" + //
            "});\n" + //
            "</script>\n\n";

        this.writeSqueezeFile(toc);
    }

    private void setBody() {
        String highlighting = ""+ 
            "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.11.1/styles/vs.css\">\n" +
            "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.11.1/highlight.min.js\"></script>\n" +
            "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.11.1/languages/java.min.js\"></script>\n" +
            "<script>hljs.highlightAll();</script>\n";

        this.writeSqueezeFile("<body>\n" + highlighting);
    }

    public void finalyzeHtml() {
        if (this.isHtml) {
            this.writeSqueezeFile("</body>\n</html>");
        }
    }

	public void printH1(String line){
        if (this.isHtml) {
            this.writeSqueezeFile("<h1 id=\""+ line.replaceAll("\\W", "") +"\">" + line + "</h1>");
        } 
        OutBut.printH1(line);
	}
	
	public void printH2(String line){
        if (this.isHtml) {
            this.writeSqueezeFile("<h2 id=\""+ line.replaceAll("\\W", "") +"\">" + line + "</h2>");
        } 
        OutBut.printH2(line);
    }
	
	public void printH3(String line){
		if (this.isHtml) {
            this.writeSqueezeFile("<h3 id=\""+ line.replaceAll("\\W", "") +"\">" + line + "</h3>");
        } 
        OutBut.printH3(line);
	}
	
	public void printStep(String line){
        if (this.isHtml) {
            this.writeSqueezeFile(">>>>>>>> " + line);
        } 
		OutBut.printStep(line);
	}
	
	public void printWarning(String line){
        if (this.isHtml) {
            this.writeSqueezeFile("!!!!!!!! WARNING: " + line);
        } 
		OutBut.printWarning(line);
	}
	
	public void printError(String line){
        if (this.isHtml) {
            this.writeSqueezeFile("XXXXX ERROR: " + line);
        }
		OutBut.printError(line);
	}

    public void printCode(String line) {
        if (this.isHtml) {
            this.writeSqueezeFile("<pre><code class=\"language-java\">" + line + "</code></pre>");
        } 
        OutBut.printNormal(line);
    }
	
	public void printNormal(String line) {
        if (this.isHtml) {
            this.writeSqueezeFile("<p>" + line + "</p>");
        }
		OutBut.printNormal(line);
	}
}
