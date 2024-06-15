from django.contrib.auth.models import Group
from rest_framework import permissions


def is_in_group(user, group_name):
    """
    Takes a user and a group name, and returns `True` if the user is in that group.
    """
    try:
        return Group.objects.get(name=group_name).user_set.filter(id=user.id).exists()
    except Group.DoesNotExist:
        return None


class HasGroupPermission(permissions.BasePermission):
    """
    Ensure user is in required groups.
    """

    def has_permission(self, request, view):
        # Mapeia métodos de requisição (GET, POST, etc.) aos grupos necessários.
        required_groups_mapping = getattr(view, "required_groups", {})

        # Determina os grupos necessários para o método de requisição específico.
        required_groups = required_groups_mapping.get(request.method, [])

        # Verifica se o usuário pertence a todos os grupos necessários ou se é um membro da equipe (staff).
        # '__all__' é um grupo especial que sempre retorna True.
        user_has_groups = all(
            is_in_group(request.user, group_name) if group_name != "__all__" else True
            for group_name in required_groups
        )

        # Retorna True se o usuário pertence a todos os grupos necessários ou se é um membro da equipe (staff).
        return user_has_groups or (request.user and request.user.is_staff)
