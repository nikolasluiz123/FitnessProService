from django.contrib.auth.models import User
from rest_framework.settings import api_settings
from rest_framework.viewsets import *

from authentication.serializers import UserSerializer
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
