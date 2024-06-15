from django.contrib.auth.models import User, Group
from email_validator import validate_email, EmailNotValidError
from rest_framework import serializers

from authentication.enumerators import EnumUserProfile


class UserSerializer(serializers.ModelSerializer):
    user_profile = serializers.ChoiceField(choices=EnumUserProfile.choices(), write_only=True)

    class Meta:
        model = User
        fields = '__all__'

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        self.fields['username'].error_messages = {
            'required': 'O nome de usuário é obrigatório.',
            'null': 'O nome de usuário é obrigatório.',
            'blank': 'O nome de usuário é obrigatório.'
        }

        self.fields['password'].error_messages = {
            'required': 'A senha é obrigatória.',
            'null': 'A senha é obrigatória.',
            'blank': 'A senha é obrigatória.'
        }

    @staticmethod
    def validate_first_name(first_name: str):
        if first_name is None or len(first_name) == 0:
            raise serializers.ValidationError('O nome é obrigatório.')

        return first_name

    @staticmethod
    def validate_last_name(last_name: str):
        if last_name is None or len(last_name) == 0:
            raise serializers.ValidationError('O sobrenome é obrigatório.')

        return last_name

    @staticmethod
    def validate_email(email: str):
        if email is None or len(email) == 0:
            raise serializers.ValidationError('O e-mail é obrigatório.')

        try:
            validate_email(email, check_deliverability=True)
        except EmailNotValidError:
            raise serializers.ValidationError('O e-mail é inválido.')

        user_exists_with_email = User.objects.filter(email=email).exists()
        if user_exists_with_email:
            raise serializers.ValidationError('Já existe um usuário cadastrado com esse e-mail.')

        return email

    def create(self, validated_data):
        group_name = f'{validated_data.pop('user_profile')}_PERMISSIONS'
        validated_data['groups'] = Group.objects.filter(name=group_name)

        return super().create(validated_data)
