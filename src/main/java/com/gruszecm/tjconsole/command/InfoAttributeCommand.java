package com.gruszecm.tjconsole.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.management.MBeanAttributeInfo;

import com.gruszecm.tjconsole.DataOutputService;
import com.gruszecm.tjconsole.TJContext;

public class InfoAttributeCommand extends AbstractAttributeCommand {

	public InfoAttributeCommand(TJContext context, PrintStream output) {
		super(context, output);
	}

	@Override
	protected String getPrefix() {
		return "\\a";
	}

	@Override
	protected void actionEvn(String input, String attribute) throws Exception {
		output.append("Envioment variable - ").append(attribute).append('\n');
	}
	
	@Override
	protected void actionBean(String input, String att) throws Exception {
		List<MBeanAttributeInfo> attributes = new ArrayList<MBeanAttributeInfo>(ctx.getAttributes());
		if (att.length() > 0) {
			for(Iterator<MBeanAttributeInfo> it=attributes.iterator(); it.hasNext();) {
				if (! it.next().getName().startsWith(att)) it.remove();
			}
		}
		for(MBeanAttributeInfo ai : attributes) {
			output.append('\t').append(ai.getName()).append("::");
			output.append(ai.getType());
			if (ai.isWritable() && ai.isReadable()) output.append("  WR");
			else {
				if (ai.isReadable()) output.append("  RO");
				else if (ai.isWritable()) output.append("  WO");
				else output.append("  --");
			}
			if (ai.isReadable()) {
				output.append("  (");
				Object value = ctx.getServer().getAttribute(ctx.getObjectName(), ai.getName());
				DataOutputService.get(ai.getType()).output(value, output);
				output.append(')');
			}
			output.append('\n');
		}
	}
	
	@Override
	public CommandHelp getHelp() {
		return new CommandHelp("Get attribute information.", "\\a [attributeName]", "\\a");
	}
	

}
