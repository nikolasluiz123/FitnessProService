from django.contrib.auth import authenticate
from django_filters.rest_framework import DjangoFilterBackend
from rest_framework.filters import SearchFilter
from rest_framework.permissions import AllowAny
from rest_framework.response import Response
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

    ordering = ['username', 'first_name', 'last_name']
    search_fields = ['username', 'email', 'first_name', 'last_name']
    filterset_fields = ['is_staff', 'is_active']


class AcademyFrequencyViewSet(ModelViewSet):
    serializer_class = AcademyFrequencySerializer

    pagination_class = None
    filter_backends = [DjangoFilterBackend, SearchFilter]

    def get_queryset(self):
        username = self.request.query_params.get('search', None)
        queryset = AcademyFrequency.objects.all()
        day_week_ordering = AcademyFrequency.get_day_week_ordering_case()

        if username:
            queryset = queryset.filter(
                academy__in=UserAcademy.objects.filter(user__username=username).values('academy')
            )

        return queryset.annotate(day_order=day_week_ordering).order_by('day_order')


class AcademiesListAPI(generics.ListAPIView):
    """
        Listagem de todas as academias sem paginação. Pode ser usada quando deseja-se exibir todos os dados de uma
        vez em algum componente simples de visualização.
    """

    serializer_class = ListAcademiesSerializer
    pagination_class = None

    required_groups = {
        'PATCH': ['SUPER_USER_PERMISSIONS'],
        'PUT': ['SUPER_USER_PERMISSIONS'],
        'DELETE': ['SUPER_USER_PERMISSIONS'],
    }

    ordering = ['name']

    def get_permissions(self):
        if self.request.method == 'GET':
            permission_classes = []
        else:
            permission_classes = api_settings.DEFAULT_PERMISSION_CLASSES + [HasGroupPermission]

        return [permission() for permission in permission_classes]

    def get_queryset(self):
        query_set = Academy.objects.all()
        return query_set


class UserAuthenticationView(generics.GenericAPIView):
    permission_classes = [AllowAny]
    serializer_class = UserAuthenticationSerializer

    @staticmethod
    def post(request, *args, **kwargs):
        serializer = UserAuthenticationSerializer(data=request.data)

        serializer.is_valid(raise_exception=True)

        username = serializer.validated_data['username']
        password = serializer.validated_data['password']

        user = authenticate(request, username=username, password=password)

        if user is None:
            raise serializers.ValidationError('As credenciais são inválidas, por favor, redigite.',
                                              code=status.HTTP_401_UNAUTHORIZED)

        user_serializer = UserSerializer(user)

        return Response(user_serializer.data, status=status.HTTP_200_OK)
