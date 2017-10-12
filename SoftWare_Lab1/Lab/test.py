import os
os.environ['CLASSPATH'] = "D://Eclipse-Workplace/LAB.jar"

import json

def traverseGraph(graphStr):
    vertices = graphStr[0].split(',')
    edges = []
    for line in graphStr[1:]:
        nodes = line.strip().split("\t")
        for nodestr in nodes[1:]:
            node = nodestr.split(',')
            edge = (vertices.index(nodes[0], vertices.index(node[0]), vertices.index(node[1])))
            edges.append(edge)
    return vertices, edges

def printGraph(graph):
    for vertex in graph[0]:
        print(vertex, end='')
    print()

    for edge in graph[1]:
        print(edge)
    print()

if __name__ == '__main__':
    # from jnius import autoclass
    # lab = autoclass('liu.zhang.PairProgramming')
    # lab.readText("D://Eclipse-Workplace")
    # lab.generateGraph()
    # graphStr = list(lab.showDirectedGraph())
    # printGraph(traverseGraph(graphStr))
    v = {"zhag":[("agd", 1),("asg",2)], "Luo":[("saf", 5), ("asf", 5)] }
    jsonv = json.dumps(v)
    print(jsonv)