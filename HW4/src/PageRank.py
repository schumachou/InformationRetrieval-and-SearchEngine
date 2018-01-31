import networkx as nx

G = nx.read_edgelist("edgeList.txt", create_using=nx.DiGraph())
pr = nx.pagerank(G, alpha=0.85, personalization=None, max_iter=30, tol=1e-06, nstart=None, weight='weight', dangling=None)
# print(pr)
f = open('external_pageRankFile.txt', 'w')
for k in pr:
    print("/Users/ChrisChou/Sites/solr-php-client-master/solr-7.1.0/WSJ/WSJ/{}={}".format(k, pr[k]), file = f)
f.close()