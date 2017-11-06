//package codesmell.smellRules;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.expr.MethodCallExpr;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import codesmell.AbstractSmell;
//import codesmell.SmellyElement;
//import codesmell.TestMethod;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BulkDataTransferOnSlowNetworkRule extends AbstractSmell {
//
//    private List<SmellyElement> smellyElementList;
//
//    public BulkDataTransferOnSlowNetworkRule() {
//        smellyElementList = new ArrayList<>();
//    }
//
//    /**
//     * Checks of 'Assertion Roulette' smellRules
//     */
//    @Override
//    public String getSmellName() {
//        return "Bulk Data Transfer On Slow NetworkRule";
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
//     * Analyze the test file for test methods for multiple assert statements without an explanation/message
//     */
//    @Override
//    public void runAnalysis(CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
//
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
//
//    private class ClassVisitor extends VoidVisitorAdapter<Void> {
//        private MethodDeclaration currentMethod = null;
//        private int assertMessageCount = 0;
//        private int assertCount = 0;
//        TestMethod testMethod;
//
//        // examine all methods in the test class
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            //only analyze methods that either have a @test annotation (Junit 4) or the method name starts with 'test'
//            if (n.getAnnotationByName("Test").isPresent() || n.getNameAsString().toLowerCase().startsWith("test")) {
//                currentMethod = n;
//                testMethod = new TestMethod(n.getNameAsString());
//                testMethod.setHasSmell(false); //default value is false (i.e. no smellRules)
//                super.visit(n, arg);
//
//
//                // if there is only 1 assert statement in the method, then a explanation message is not needed
//                if(assertCount==1)
//                    testMethod.setHasSmell(false);
//                else if(assertCount >=2 && assertMessageCount >=1) //if there is more than one assert statement, then all the asserts need to have an explanation message
//                    testMethod.setHasSmell(true);
//
//                testMethod.addDataItem("AssertCount", String.valueOf(assertMessageCount));
//
//                smellyElementList.add(testMethod);
//
//                //reset values for next method
//                currentMethod = null;
//                assertCount=0;
//                assertMessageCount = 0;
//            }
//        }
//
//        // examine the methods being called within the test method
//        @Override
//        public void visit(MethodCallExpr n, Void arg) {
//            super.visit(n, arg);
//            if (currentMethod != null) {
//                // if the name of a method being called start with 'assert'
//                if (n.getNameAsString().startsWith(("assert"))) {
//                    assertCount++;
//                    // assert methods that do not contain a message
//                    if (n.getArguments().size() < 3) {
//                        assertMessageCount++;
//                    }
//                }
//                // if the name of a method being called is 'fail'
//                else if (n.getNameAsString().equals("fail")) {
//                    assertCount++;
//                    // fail method does not contain a message
//                    if (n.getArguments().size() < 1) {
//                        assertMessageCount++;
//                    }
//                }
//
//            }
//        }
//
//    }
//}
//
