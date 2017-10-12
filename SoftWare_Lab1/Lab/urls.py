from . import  views
from django.conf.urls import url

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^query_word$', views.queryBridgeWord, name='query_bridge_word'),
    url(r'^generate_txt$', views.generateTxt, name='generate_txt'),
    url(r'^show_path$', views.showMinPath, name='show_min_path'),
    url(r'^calc_all_path$', views.calcAllMinPath, name='calc_all_min_path'),
    url(r'^show_one_path$', views.showOneMinPath, name='show_one_min_path'),
    url(r'^random_walk$', views.randomWalk, name='random_walk'),
    url(r'^stop_walk$', views.stopWalk, name='stop_walk'),
]