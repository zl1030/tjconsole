package org.jsoftware.tjconsole.command;

import org.jsoftware.tjconsole.DataOutputService;
import org.jsoftware.tjconsole.Output;
import org.jsoftware.tjconsole.TJContext;

import javax.management.MBeanAttributeInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Command to get attribute value of mxBean
 *
 * @author szalik
 */
public class GetAttributeCommand extends AbstractAttributeCommand {

    public GetAttributeCommand(TJContext context, Output output) {
        super(context, output);
    }

    @Override
    protected String getPrefix() {
        return "GET";
    }

    @Override
    public void actionBean(String input, String att) throws Exception {
        List<MBeanAttributeInfo> attributes = new ArrayList<MBeanAttributeInfo>(ctx.getAttributes());
        if (att.length() > 0) {
            for (Iterator<MBeanAttributeInfo> it = attributes.iterator(); it.hasNext(); ) {
                if (!it.next().getName().startsWith(att)) it.remove();
            }
        }
        StringBuilder sb = new StringBuilder();
        for (MBeanAttributeInfo ai : attributes) {
            sb.append(ai.getName()).append(" = ");
            Object value = ctx.getServer().getAttribute(ctx.getObjectName(), ai.getName());
            DataOutputService.get(ai.getType()).output(value, sb);
            sb.append('\n');
        }
        output.outMBeanOutput(sb.toString());
    }

    @Override
    protected void actionEvn(String input, String attribute) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(attribute).append(" = ");
        sb.append(ctx.getEnvironment().get(attribute).toString());
        sb.append('\n');
        output.outMBeanOutput(sb.toString());
    }

    @Override
    public CmdDescription getHelp() {
        return new CmdDescription("Get attribute value", "GET [attributeName]", "GET");
    }

}