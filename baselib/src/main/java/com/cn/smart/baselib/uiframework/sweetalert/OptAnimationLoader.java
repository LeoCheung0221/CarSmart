package com.cn.smart.baselib.uiframework.sweetalert;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * author：leo on 2016/11/17 15:10
 * email： leocheung4ever@gmail.com
 * description: 操作动画加载器
 * what & why is modified:
 */

public class OptAnimationLoader {

    public static Animation loadAnimation(Context context, int animId) throws Resources.NotFoundException {
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(animId);
            return createAnimationFromXml(context, parser);
        } catch (XmlPullParserException | IOException ex) {
            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x"
                    + Integer.toHexString(animId));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null)
                parser.close();
        }
    }

    private static Animation createAnimationFromXml(Context context, XmlPullParser parser)
            throws XmlPullParserException, IOException {
        return createAnimationFromXml(context, parser, null, Xml.asAttributeSet(parser));
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser,
                                                    AnimationSet parent, AttributeSet attrs)
            throws XmlPullParserException, IOException {

        Animation anim = null;
        //确保有一个开始动画的标识
        int type;
        int depth = parser.getDepth();

        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {

            String name = parser.getName();
            switch (name) {
                case "set":
                    anim = new AnimationSet(c, attrs);
                    createAnimationFromXml(c, parser, (AnimationSet) anim, attrs);
                    break;
                case "alpha":
                    anim = new AlphaAnimation(c, attrs);
                    break;
                case "scale":
                    anim = new ScaleAnimation(c, attrs);
                    break;
                case "rotate":
                    anim = new RotateAnimation(c, attrs);
                    break;
                case "translate":
                    anim = new TranslateAnimation(c, attrs);
                    break;
                default:
                    try {
                        anim = (Animation) Class.forName(name).getConstructor(Context.class, AttributeSet.class).newInstance(c, attrs);
                    } catch (Exception te) {
                        throw new RuntimeException("Unknown animation name: " + parser.getName() + " error:" + te.getMessage());
                    }
                    break;
            }

            if (parent != null) {
                parent.addAnimation(anim);
            }
        }

        return anim;
    }
}
