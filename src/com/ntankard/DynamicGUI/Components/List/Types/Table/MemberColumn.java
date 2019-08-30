package com.ntankard.DynamicGUI.Components.List.Types.Table;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CalendarDecoder;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CurrencyDecoder;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.Decoder;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.DoubleDecoder;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Renderer.NegativeHighlightRenderer;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Renderer.Renderer;

import java.util.Calendar;
import java.util.Locale;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_BELOW_BAD;
import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_BINARY;
import static com.ntankard.ClassExtension.DisplayProperties.DataType;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.*;

public class MemberColumn {

    /**
     * The core member
     */
    private Member member;

    /**
     * The display order, Integer.MAX_VALUE if none is set
     */
    private int order = Integer.MAX_VALUE;

    /**
     * The display name of the column
     */
    private String name = "";

    /**
     * Any custom render for this column
     */
    private Renderer renderer = null;

    /**
     * Constructor, parameters are set from the DisplayProperties set to the member
     *
     * @param member The member this column is based around
     */
    MemberColumn(Member member) {
        this.member = member;

        // Extract any properties
        DisplayProperties properties = this.member.getGetter().getAnnotation(DisplayProperties.class);
        DataType dataType = AS_CLASS;
        if (properties != null) {
            order = properties.order();
            name = properties.name();
            dataType = properties.dataType();

            if (properties.dataContext() == ZERO_BELOW_BAD) {
                renderer = new NegativeHighlightRenderer(false);
            } else if (properties.dataContext() == ZERO_BINARY) {
                renderer = new NegativeHighlightRenderer(true);
            }
        }

        // Fill in any missing properties
        if (renderer == null) {
            renderer = new Renderer();
        }
        if (name.equals("")) {
            name = member.getName();
        }

        // Build the decoder based on type
        Decoder coder = null;
        if (member.getType().equals(Calendar.class)) {
            coder = new CalendarDecoder();
        } else if (member.getType().equals(Double.class)) {
            if (dataType.equals(CURRENCY_AUD)) {
                coder = new CurrencyDecoder(Locale.US);
            } else if (dataType.equals(CURRENCY_YEN)) {
                coder = new CurrencyDecoder(Locale.JAPAN);
            } else {
                coder = new DoubleDecoder();
            }
        }
        renderer.setDecoder(coder);
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public Member getMember() {
        return member;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
