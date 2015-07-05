__author__ = 'duncan'

import glob
import re
import os

subs = (
    {},
    {},
    {
        'Func1': 'Func2',
        'Proc1': 'Proc2',
        '<P1,': '<P1, P2,',
        '(P1 p1)': '(P1 p1, P2 p2)',
        '(null)': '(null, null)',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1)'
    },
    {
        'Func1': 'Func3',
        'Proc1': 'Proc3',
        '<P1,': '<P1, P2, P3,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3)',
        '(null)': '(null, null, null)',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2)'
    },
    {
        'Func1': 'Func4',
        'Proc1': 'Proc4',
        '<P1,': '<P1, P2, P3, P4,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3, P4 p4)',
        '(null)': '(null, null, null, null)',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2), \n' +
                                           '(P4) invocation.getParameter(3)'
    },
    {
        'Func1': 'Func5',
        'Proc1': 'Proc5',
        '<P1,': '<P1, P2, P3, P4, P5,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5)',
        '(null)': '(null, null, null, null, null)',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2), \n' +
                                           '(P4) invocation.getParameter(3), (P5) invocation.getParameter(4)'
    },
    {
        'Func1': 'Func6',
        'Proc1': 'Proc6',
        '<P1,': '<P1, P2, P3, P4, P5, P6,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6)',
        '(null)': '(null, null, null, null, null, null)',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2), \n' +
                                           '(P4) invocation.getParameter(3), (P5) invocation.getParameter(4), \n' +
                                           '(P6) invocation.getParameter(5)'
    },
    {
        'Func1': 'Func7',
        'Proc1': 'Proc7',
        '<P1,': '<P1, P2, P3, P4, P5, P6, P7,',
        '(P1 p1)': '(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6, P7 p7)',
        '(null)': '(null, null, null, null, null, null, null)',
        '(P1) invocation.getParameter(0)': '(P1) invocation.getParameter(0), \n' +
                                           '(P2) invocation.getParameter(1), (P3) invocation.getParameter(2), \n' +
                                           '(P4) invocation.getParameter(3), (P5) invocation.getParameter(4), \n' +
                                           '(P6) invocation.getParameter(5), (P7) invocation.getParameter(6)'
    },
)

sources = glob.glob("src/main/java/org/jmock/function/????1*.java")


def output_path(input_path, arity):
    return re.sub(r'1(.*.java)', str(arity) + r"\1", input_path.replace('src', 'target/generated-sources'))


def replace(line, subs):
    for key in subs.keys():
        line = line.replace(key, subs[key])
    return line


def replace_lines(lines, subs):
    for line in lines:
        yield replace(line, subs)


def generate(source, subs):
    lines = open(source)
    return replace_lines(lines, subs)


def create_parent(path):
    parent = os.path.dirname(path)
    if not os.path.exists(parent):
        os.makedirs(parent)


for arity in range(2, 8):
    for source in sources:
        path = output_path(source, arity)
        create_parent(path)
        output = open(path, "w")
        for line in generate(source, subs[arity]):
            output.write(line)
