from django.contrib.auth.models import User, Group
from rest_framework import serializers

from authentication.enumerators import EnumUserProfile


class UserSerializer(serializers.ModelSerializer):
    user_profile = serializers.ChoiceField(choices=EnumUserProfile.choices(), write_only=True)

    class Meta:
        model = User
        fields = '__all__'
        
    def create(self, validated_data):
        group_name = f'{validated_data.pop('user_profile')}_PERMISSIONS'
        validated_data['groups'] = Group.objects.filter(name=group_name)

        return super().create(validated_data)

