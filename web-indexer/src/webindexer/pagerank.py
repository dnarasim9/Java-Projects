import networkx as nx
import csv
import timeit

reader = csv.reader(open('pagerankdata.csv'))
writer = csv.writer(open('pageRank.csv', 'wb'))
fileWriter = open('external_pagerank','w')

result = {}
start = timeit.default_timer()
print 'Reading Input......'
for row in reader:
    key = row[0]
    result[key] = row[1:]
print 'Creating NetworkX DiGraph.....'
G = nx.DiGraph()
# print 'Adding Nodes....'
# G.add_nodes_from(result.keys())
print 'Adding Edges...'
for node in result.keys():
    for child in result[node]:
        # G.add_nodes_from(result[node])
        G.add_edge(node, child)
print 'Calculating Rank Score..'
pr = nx.pagerank(G, alpha=0.85)
print 'Writing RankScore to File.'
for key, value in pr.items():
   writer.writerow([key, value])
   fileWriter.write(key+"="+str(value)+"\n")
end = timeit.default_timer()
print 'Completed with no errors!'
print 'Elapsed Time ' + str(end - start) + 'sec'
