/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.guidebooks.jstl.tags.prettyprint;

import com.uwyn.jhighlight.renderer.*;
import com.uwyn.jhighlight.renderer.Renderer;

/**
 *
 * @author Administrator
 */
public enum PrettyPrintableLanguage {
    JAVA("java", new JavaXhtmlRenderer()),
    GROOVY("groovy", new GroovyXhtmlRenderer()),
    CSHARP("C#", new JavaXhtmlRenderer()),
    CPP("c++", new CppXhtmlRenderer()),
    XML("xml", new XmlXhtmlRenderer()),
    UNKNOWN_LANGUAGE("undefined",new NullRenderer());

    private Renderer prettyRenderer;
    private String language;

    private PrettyPrintableLanguage(String language, Renderer prettyRenderer) {
        this.language = language;
        this.prettyRenderer = prettyRenderer;
    }

    public Renderer getRenderer() {
        return prettyRenderer;
    }

    public String getLanguage() {
        return language;
    }

    public static PrettyPrintableLanguage getPrettyPrintLanguage(String language) {
        for(PrettyPrintableLanguage curr : values()) {
            if(curr.language.equals(language))
                return curr;
        }
        return PrettyPrintableLanguage.UNKNOWN_LANGUAGE;
    }
}
