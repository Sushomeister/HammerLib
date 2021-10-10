function initializeCoreMod()
{
    return {
        'recipemanager': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.resources.DataPackRegistries',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/command/Commands$EnvironmentType;I)V'
            },
            'transformer': function(method)
            {
                print('[HammerLib]: Patching DataPackRegistries#<init>');

                var owner = "org/zeith/hammerlib/core/RecipeHelper";
                var name = "reload";
                var desc = "(Lnet/minecraft/item/crafting/RecipeManager;Lnet/minecraft/resources/IReloadableResourceManager;)V";
                var instr = method.instructions;

                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
				var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');

				var list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/resources/DataPackRegistries", ASMAPI.mapField("field_240954_d_"), "Lnet/minecraft/item/crafting/RecipeManager;"));
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/resources/DataPackRegistries", ASMAPI.mapField("field_240952_b_"), "Lnet/minecraft/resources/IReloadableResourceManager;"));
                var methodInsn = ASMAPI.buildMethodCall(owner, name, desc, ASMAPI.MethodType.STATIC);
				list.add(methodInsn);

                var node;

				for(var i = 0; i < instr.size(); i++)
				{
					var ins = instr.get(i);
					if(ins.getOpcode() == Opcodes.INVOKESTATIC)
					{
						node = ins;
						break;
					}
				}
                instr.insert(node, list);

                return method;
            }
        }
    }
}