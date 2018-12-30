package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolTable extends Tab {

	public static final Struct boolType = new Struct(Struct.Bool);

	public static final String THIS_OBJ_NAME = "this";

	private static Map<Struct, List<Obj>> classToMethodsMap = new HashMap<>();

	private static List<Obj> getParentClassMethods(Struct classStruct) {
		List<Obj> parentClassMethods = new ArrayList<>();

		if (classStruct == null) {
			return parentClassMethods;
		}

		return Stream.concat(
				classToMethodsMap.get(classStruct).stream(),
				getParentClassMethods(classStruct.getElemType()).stream())
			.collect(Collectors.toList());
	}

	public static void addClass(Struct classStruct) {
		// fill it with all parent methods first
		classToMethodsMap.put(classStruct, new ArrayList<>());
	}

	public static void addClassMethod(Struct classStruct, Obj classMethod) {
		classToMethodsMap.get(classStruct).add(classMethod);
	}

	public static void chainParentMethods(Struct classStruct) {
		classToMethodsMap.get(classStruct)
				.addAll(getParentClassMethods(classStruct.getElemType()));
	}

	public static List<Obj> getClassMethods(Struct classStruct) {
		return classToMethodsMap.get(classStruct);
	}

	public static void completeInit() {
		SymbolTable.init();
		SymbolTable.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
	}
}
