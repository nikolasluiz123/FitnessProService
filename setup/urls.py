from django.contrib import admin
from django.urls import path, include
from drf_yasg import openapi
from drf_yasg.views import get_schema_view
from rest_framework import routers, permissions

from authentication.views import *

schema_view = get_schema_view(
    openapi.Info(
        title="Fitness Pro Service",
        default_version='v1',
        description="Serviço para tratar todas as requisições dos clientes que utilizam as soluções da FitnessPro",
        terms_of_service="#",
        contact=openapi.Contact(email="nikolas.luiz.schmitt@gmail.com"),
        license=openapi.License(name="BSD License"),
    ),
    public=True,
    permission_classes=[permissions.AllowAny],
)

router = routers.DefaultRouter()
router.register('users', UserViewSet, basename='Register User')
router.register('academies/frequencies', AcademyFrequencyViewSet, basename='Frequencies')

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', include(router.urls)),
    path('academies/list', AcademiesListAPI.as_view()),
    path('users/authenticate', UserAuthenticationView.as_view()),
    path('swagger/', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),

]
