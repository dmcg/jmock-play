
import glob
import re
import os

subs = (
    {},
    {},
    {
        'int ARITY = 1;' : 'int ARITY = 2;',
        'Func1': 'Func2',
        'Proc1': 'Proc2',
        '<P1,': '<P1, P2,',
        '(P1 p1)': '(P1 p1, P2 p2)',
        '(p1)': '(p1, p2)',
        '(Matcher<P1> p1)': '(Matcher<P1> p1, Matcher<P2> p2)',
        '(Predicate<P1> p1)': '(Predicate <P1> p1, Predicate<P2> p2)',
        '(null)': '(null, null)',
        '((P1) args[0])' : '((P1) args[0], (P2) args[1])',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1)'
    },
    {
        'int ARITY = 1;' : 'int ARITY = 3;',
        'Func1': 'Func3',
        'Proc1': 'Proc3',
        '<P1,': '<P1, P2, P3,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3)',
        '(p1)': '(p1, p2, p3)',
        '(Matcher<P1> p1)': '(Matcher<P1> p1, Matcher<P2> p2, Matcher<P3> p3)',
        '(Predicate<P1> p1)': '(Predicate <P1> p1, Predicate<P2> p2, Predicate<P3> p3)',
        '(null)': '(null, null, null)',
        '((P1) args[0])' : '((P1) args[0], (P2) args[1], (P3) args[2])',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2)'
    },
    {
        'int ARITY = 1;' : 'int ARITY = 4;',
        'Func1': 'Func4',
        'Proc1': 'Proc4',
        '<P1,': '<P1, P2, P3, P4,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3, P4 p4)',
        '(p1)': '(p1, p2, p3, p4)',
        '(Matcher<P1> p1)': '(Matcher<P1> p1, Matcher<P2> p2, Matcher<P3> p3, Matcher<P4> p4)',
        '(Predicate<P1> p1)': '(Predicate <P1> p1, Predicate<P2> p2, Predicate<P3> p3, Predicate<P4> p4)',
        '(null)': '(null, null, null, null)',
        '((P1) args[0])' : '((P1) args[0], (P2) args[1], (P3) args[2], (P4) args[3])',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2), \n' +
                                           '(P4) invocation.getParameter(3)'
    },
    {
        'int ARITY = 1;' : 'int ARITY = 5;',
        'Func1': 'Func5',
        'Proc1': 'Proc5',
        '<P1,': '<P1, P2, P3, P4, P5,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5)',
        '(p1)': '(p1, p2, p3, p4, p5)',
        '(Matcher<P1> p1)': '(Matcher<P1> p1, Matcher<P2> p2, Matcher<P3> p3, Matcher<P4> p4, Matcher<P5> p5)',
        '(Predicate<P1> p1)': '(Predicate <P1> p1, Predicate<P2> p2, Predicate<P3> p3, Predicate<P4> p4, Predicate<P5> p5)',
        '(null)': '(null, null, null, null, null)',
        '((P1) args[0])' : '((P1) args[0], (P2) args[1], (P3) args[2], (P4) args[3], (P5) args[4])',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2), \n' +
                                           '(P4) invocation.getParameter(3), (P5) invocation.getParameter(4)'
    },
    {
        'int ARITY = 1;' : 'int ARITY = 6;',
        'Func1': 'Func6',
        'Proc1': 'Proc6',
        '<P1,': '<P1, P2, P3, P4, P5, P6,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6)',
        '(p1)': '(p1, p2, p3, p4, p5, p6)',
        '(Matcher<P1> p1)': '(Matcher<P1> p1, Matcher<P2> p2, Matcher<P3> p3, Matcher<P4> p4, Matcher<P5> p5, Matcher<P6> p6)',
        '(Predicate<P1> p1)': '(Predicate <P1> p1, Predicate<P2> p2, Predicate<P3> p3, Predicate<P4> p4, Predicate<P5> p5, Predicate<P6> p6)',
        '(null)': '(null, null, null, null, null, null)',
        '((P1) args[0])' : '((P1) args[0], (P2) args[1], (P3) args[2], (P4) args[3], (P5) args[4], (P6) args[5])',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2), \n' +
                                           '(P4) invocation.getParameter(3), (P5) invocation.getParameter(4), \n' +
                                           '(P6) invocation.getParameter(5)'
    },
    {
        'int ARITY = 1;' : 'int ARITY = 7;',
        'Func1': 'Func7',
        'Proc1': 'Proc7',
        '<P1,': '<P1, P2, P3, P4, P5, P6, P7,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7)',
        '(p1)': '(p1, p2, p3, p4, p5, p7)',
        '(Matcher<P1> p1)': '(Matcher<P1> p1, Matcher<P2> p2, Matcher<P3> p3, Matcher<P4> p4, Matcher<P5> p5, Matcher<P6> p6, Matcher<P7> p7)',
        '(Predicate<P1> p1)': '(Predicate <P1> p1, Predicate<P2> p2, Predicate<P3> p3, Predicate<P4> p4, Predicate<P5> p5, Predicate<P6> p6, Predicate<P7> p7)',
        '(null)': '(null, null, null, null, null, null, null)',
        '((P1) args[0])' : '((P1) args[0], (P2) args[1], (P3) args[2], (P4) args[3], (P5) args[4], (P6) args[5], (P7) args[6])',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2), \n' +
                                           '(P4) invocation.getParameter(3), (P5) invocation.getParameter(4), \n' +
                                           '(P6) invocation.getParameter(5), (P7) invocation.getParameter(6)'
    },
)


def output_path(input_path, arity):
    return re.sub(r'1(.*.java)', str(arity) + r"\1", input_path.replace('src', 'target/generated-sources'))


def replace_from_dict(line, subs_as_dict):
    for key in subs_as_dict.keys():
        line = line.replace(key, subs_as_dict[key])
    return line


def generate(source_path, subs):
    lines = open(source_path)
    return (replace_from_dict(line, subs) for line in lines)


def create_parent(path):
    parent = os.path.dirname(path)
    if not os.path.exists(parent):
        os.makedirs(parent)


def generate_class(source_path, output_path, arity):
    create_parent(output_path)
    output = open(output_path, "w")
    for line in generate(source_path, subs[arity]):
        output.write(line)


def generate_classes(source_paths, end_artiry):
    for arity in range(2, end_artiry):
        for source_path in source_paths:
            generate_class(source_path, output_path(source_path, arity), arity)


def write_expecations_arities(output, end_arity):
    arity1_template_lines = [line for line in open('src/main/java/org/jmock/function/internal/Expec8ions1.java') if line.startswith(' ')]
    for arity in range(2, end_arity):
        for line in (replace_from_dict(line, subs[arity]) for line in arity1_template_lines):
            output.write(line)


def generate_expec8ions(end_arity):
    template_lines = open('src/main/java/org/jmock/function/Expec8ions.java.template')
    path = 'target/generated-sources/main/java/org/jmock/function/Expec8ions.java'
    create_parent(path)
    output = open(path, "w")
    for line in template_lines:
        if line.find('/* CONTENT-HERE */') > 0:
            write_expecations_arities(output, end_arity)
        else:
            output.write(line)

    pass

end_arity = 8
sources = glob.glob("src/main/java/org/jmock/function/????1*.java")
generate_classes(sources, end_arity)
generate_expec8ions(end_arity)