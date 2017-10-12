<<<<<<< HEAD
import os
os.environ['CLASSPATH'] = "C://Users/gzhang/Desktop/Lab1/out/artifacts/Lab1_jar/Lab1.jar"

import json
from jnius import autoclass
from django.shortcuts import render, HttpResponse
from django.views.decorators.csrf import csrf_exempt

lab = None
edges = None
vertices = None

def generateGraph(catelogue = "C://Users/gzhang/Desktop/Lab1"):
    """选取文本生成有向图"""
    global lab
    lab = autoclass('liu.zhang.PairProgramming')
    lab.readText(catelogue)
    lab.generateGraph()

def getDirectedGraph():
    """获取有向图的边和顶点"""
    graphStr = list(lab.showDirectedGraph())
    vertices = graphStr[0].split(',')
    edges = [edge.split('\t') for edge in graphStr[1:]]
    edges = [[vertices.index(a), vertices.index(b),int(c)] for a, b, c in [edge.split('\t') for edge in graphStr[1:]]]
    for edge in edges: edge.append(1)
    return vertices, edges

def index(request):
    """主页"""
    generateGraph()
    global vertices, edges
    vertices, edges = getDirectedGraph()
    return render(request, 'lab/index.html', {'vertices' : json.dumps(vertices), 'edges': json.dumps(edges)})

@csrf_exempt
def queryBridgeWord(request):
    """查询桥接词"""
    word1 = request.POST.get('word1')
    word2 = request.POST.get('word2')
    retValue = lab.queryBridgeWords(word1, word2).split(',')
    status, words = int(retValue[0]), retValue[1:]
    result = {"status": status, "words": words}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def generateTxt(request):
    """生成新文本"""
    inputText = request.POST.get('inputText')
    newText = lab.generateNewText(inputText)
    result = {'newText': newText}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def showMinPath(request):
    """查询两点间最短路径"""
    startWord = request.POST.get('startWord')
    endWord = request.POST.get('endWord')
    retValue = lab.calcShortestPath(startWord, endWord).split('@')
    status, dist, paths = int(retValue[0]), int(retValue[1]), retValue[2].split(';')
    result = {'paths': paths, 'dist': dist, 'status': status}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def calcAllMinPath(request):
    """计算单源最短路径"""
    startWord = request.POST.get('startWord')
    retValue = lab.calcAllShortestPath(startWord)
    status = int(retValue)
    result = {'status': status}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def showOneMinPath(request):
    """查询源点到指定顶点的最短路径"""
    endWord = request.POST.get('endWord')
    retValue = lab.showOneShortestPath(endWord).split('@')
    status, dist, path = int(retValue[0]), int(retValue[1]), retValue[2]
    result = {'path': path, 'dist': dist, 'status': status}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def randomWalk(request):
    """随机游走"""
    word = request.POST.get('word')
    retValue = lab.randomWalk(word).split('@')
    status, randomPath = int(retValue[0]), retValue[1]
    result = {'path': randomPath, 'status': status}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def stopWalk(request):
    """游走结束或停止游走"""
    stopPos = str(request.POST.get('stopPos'))
    status = int(lab.writeRandomPath(stopPos))
    return HttpResponse(json.dumps({'status': status}), content_type='application/json')

if __name__ == '__main__':
    pass
=======
import os
os.environ['CLASSPATH'] = "C://Users/gzhang/Desktop/Lab1/out/artifacts/Lab1_jar/Lab1.jar"

import json
from jnius import autoclass
from django.shortcuts import render, HttpResponse
from django.views.decorators.csrf import csrf_exempt

lab = None
edges = None
vertices = None

def generateGraph(catelogue = "C://Users/gzhang/Desktop/Lab1"):
    """选取文本生成有向图"""
    global lab
    lab = autoclass('liu.zhang.PairProgramming')
    lab.readText(catelogue)
    lab.generateGraph()

def getDirectedGraph():
    """获取有向图的边和顶点"""
    graphStr = list(lab.showDirectedGraph())
    vertices = graphStr[0].split(',')
    edges = [edge.split('\t') for edge in graphStr[1:]]
    edges = [[vertices.index(a), vertices.index(b),int(c)] for a, b, c in [edge.split('\t') for edge in graphStr[1:]]]
    for edge in edges: edge.append(1)
    return vertices, edges

def index(request):
    """主页"""
    generateGraph()
    global vertices, edges
    vertices, edges = getDirectedGraph()
    return render(request, 'lab/index.html', {'vertices' : json.dumps(vertices), 'edges': json.dumps(edges)})

@csrf_exempt
def queryBridgeWord(request):
    """查询桥接词"""
    word1 = request.POST.get('word1')
    word2 = request.POST.get('word2')
    retValue = lab.queryBridgeWords(word1, word2).split(',')
    status, words = int(retValue[0]), retValue[1:]
    result = {"status": status, "words": words}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def generateTxt(request):
    """生成新文本"""
    inputText = request.POST.get('inputText')
    newText = lab.generateNewText(inputText)
    result = {'newText': newText}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def showMinPath(request):
    """查询两点间最短路径"""
    startWord = request.POST.get('startWord')
    endWord = request.POST.get('endWord')
    retValue = lab.calcShortestPath(startWord, endWord).split('@')
    status, dist, paths = int(retValue[0]), int(retValue[1]), retValue[2].split(';')
    result = {'paths': paths, 'dist': dist, 'status': status}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def calcAllMinPath(request):
    """计算单源最短路径"""
    startWord = request.POST.get('startWord')
    retValue = lab.calcAllShortestPath(startWord)
    status = int(retValue)
    result = {'status': status}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def showOneMinPath(request):
    """查询源点到指定顶点的最短路径"""
    endWord = request.POST.get('endWord')
    retValue = lab.showOneShortestPath(endWord).split('@')
    status, dist, path = int(retValue[0]), int(retValue[1]), retValue[2]
    result = {'path': path, 'dist': dist, 'status': status}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def randomWalk(request):
    """随机游走"""
    word = request.POST.get('word')
    retValue = lab.randomWalk(word).split('@')
    status, randomPath = int(retValue[0]), retValue[1]
    result = {'path': randomPath, 'status': status}
    return HttpResponse(json.dumps(result), content_type='application/json')

@csrf_exempt
def stopWalk(request):
    """游走结束或停止游走"""
    stopPos = str(request.POST.get('stopPos'))
    status = int(lab.writeRandomPath(stopPos))
    return HttpResponse(json.dumps({'status': status}), content_type='application/json')

if __name__ == '__main__':
    pass
>>>>>>> liuyan
