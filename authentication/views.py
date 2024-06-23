from rest_framework.settings import api_settings
from rest_framework.viewsets import *

from authentication.serializers import *
from authentication.user_groups import HasGroupPermission


class UserViewSet(ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer

    def get_permissions(self):
        if self.request.method == 'GET' or self.request.method == 'DELETE':
            permission_classes = api_settings.DEFAULT_PERMISSION_CLASSES + [HasGroupPermission]
        else:
            permission_classes = []

        return [permission() for permission in permission_classes]

    required_groups = {
        'GET': ['SUPER_USER_PERMISSIONS'],
        'DELETE': ['SUPER_USER_PERMISSIONS'],
    }

    ordering_fields = ['username', 'first_name', 'last_name']
    search_fields = ['username', 'email', 'first_name', 'last_name']
    filterset_fields = ['is_staff', 'is_active']


class AcademyViewSet(ModelViewSet):
    queryset = Academy.objects.all()
    serializer_class = AcademySerializer


class AcademiesListAPI(generics.ListAPIView):
    """
        Listagem de todas as academias sem paginação. Pode ser usada quando deseja-se exibir todos os dados de uma
        vez em algum componente simples de visualização.
    """

    serializer_class = ListAcademiesSerializer
    pagination_class = None

    def get_queryset(self):
        query_set = Academy.objects.all()
        return query_set
