from rest_framework import status
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

    ordering_fields = ['username', 'first_name', 'last_name']
    search_fields = ['username', 'email', 'first_name', 'last_name']
    filterset_fields = ['is_staff', 'is_active']


class AcademyViewSet(ModelViewSet):
    queryset = Academy.objects.all()
    serializer_class = AcademySerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)
        headers = self.get_success_headers(serializer.data)

        return Response(serializer.data, status=status.HTTP_201_CREATED, headers=headers)

    def update(self, request, *args, **kwargs):
        partial = kwargs.pop('partial', False)
        instance = self.get_object()
        serializer = self.get_serializer(instance, data=request.data, partial=partial)
        serializer.is_valid(raise_exception=True)
        self.perform_update(serializer)

        return Response(serializer.data)

    def perform_create(self, serializer):
        serializer.save()

    def perform_update(self, serializer):
        serializer.save()
