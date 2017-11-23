//package codesmell.smellRules;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.expr.MethodCallExpr;
//import com.github.javaparser.ast.expr.NameExpr;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import codesmell.AbstractSmell;
//import codesmell.entity.SmellyElement;
//import codesmell.Method;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//
///*
//Use of Thread.sleep() in test methods can possibly lead to unexpected results as the processing time of tasks on different devices/machines can be different. Use mock objects instead
//This code marks a Method as smelly if the Method body calls Thread.sleep()
// */
//public class UncachedViewsRule extends AbstractSmell {
//
//    private List<SmellyElement> smellyElementList;
//
//    public UncachedViewsRule() {
//        smellyElementList = new ArrayList<>();
//    }
//
//    /**
//     * Checks of 'Wait And See' smellRules
//     */
//    @Override
//    public String getSmellName() {
//        return "Wait And See";
//    }
//
//    /**
//     * Returns true if any of the elements has a smellRules
//     */
//    @Override
//    public boolean getHasSmell() {
//        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
//    }
//
//    /**
//     * Analyze the test file for test methods that use Thread.sleep()
//     */
//    @Override
//    public void runAnalysis(CompilationUnit testFileCompilationUnit,CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
//        UncachedViewsRule.ClassVisitor classVisitor;
//        classVisitor = new UncachedViewsRule.ClassVisitor();
//        classVisitor.visit(testFileCompilationUnit, null);
//    }
//
//    /**
//     * Returns the set of analyzed elements (i.e. test methods)
//     */
//    @Override
//    public List<SmellyElement> getSmellyElements() {
//        return smellyElementList;
//    }
//
//    private class ClassVisitor extends VoidVisitorAdapter<Void> {
//        private MethodDeclaration currentMethod = null;
//        private int sleepCount = 0;
//        Method testMethod;
//
//        // examine all methods in the test class
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            //only analyze methods that either have a @test annotation (Junit 4) or the Method name starts with 'test'
//            if (n.getAnnotationByName("Test").isPresent() || n.getNameAsString().toLowerCase().startsWith("test")) {
//                currentMethod = n;
//                testMethod = new Method(n.getNameAsString());
//                testMethod.setHasSmell(false); //default value is false (i.e. no smellRules)
//                super.visit(n, arg);
//
//                testMethod.setHasSmell(sleepCount >= 1);
//                testMethod.addDataItem("ThreadSleepCount", String.valueOf(sleepCount));
//
//                smellyElementList.add(testMethod);
//
//                //reset values for next Method
//                currentMethod = null;
//                sleepCount = 0;
//            }
//        }
//
//        // examine the methods being called within the test Method
//        @Override
//        public void visit(MethodCallExpr n, Void arg) {
//            super.visit(n, arg);
//            if (currentMethod != null) {
//                // if the name of a Method being called is 'sleep'
//                if (n.getNameAsString().equals("sleep")) {
//                    //check the scope of the Method
//                    if ((n.getScope().isPresent() && n.getScope().get() instanceof NameExpr)) {
//                        //proceed only if the scope is "Thread"
//                        if ((((NameExpr) n.getScope().get()).getNameAsString().equals("Thread"))) {
//                            sleepCount++;
//                        }
//                    }
//
//                }
//            }
//        }
//
//    }
//}
