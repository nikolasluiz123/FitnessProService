from django.contrib.auth.hashers import make_password
from django.contrib.auth.models import Group
from email_validator import validate_email, EmailNotValidError
from rest_framework import serializers, status

from authentication.enumerators import *
from authentication.models import *


class UserSerializer(serializers.ModelSerializer):
    user_profile = serializers.ChoiceField(choices=EnumUserProfile.choices(), write_only=True)
    group_names = serializers.SerializerMethodField()

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
    def get_group_names(obj):
        return [group.name for group in obj.groups.all()]

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
        validated_data['password'] = make_password(validated_data['password'])

        return super().create(validated_data)


class AcademyFrequencySerializer(serializers.ModelSerializer):
    day_week_display = serializers.SerializerMethodField()
    academy_name = serializers.SerializerMethodField()
    username = serializers.CharField(write_only=True)

    class Meta:
        model = AcademyFrequency
        fields = ['id', 'day_week', 'day_week_display', 'start', 'end', 'academy', 'academy_name', 'username']

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        self.fields['day_week'].error_messages = {
            'required': 'O dia da semana é obrigatório.',
            'null': 'O dia da semana é obrigatório.',
            'blank': 'O dia da semana é obrigatório.',
            'invalid_choice': 'Dia da semana inválido.'
        }

        self.fields['start'].error_messages = {
            'required': 'A hora de início é obrigatória.',
            'null': 'A hora de início é obrigatória.',
            'blank': 'A hora de início é obrigatória.',
            'invalid': 'A hora de início é inválida.'
        }

        self.fields['end'].error_messages = {
            'required': 'A hora de fim é obrigatória.',
            'null': 'A hora de fim é obrigatória.',
            'blank': 'A hora de fim é obrigatória.',
            'invalid': 'A hora de fim é inválida.'
        }

    @staticmethod
    def get_day_week_display(obj):
        return obj.get_day_week_display()

    @staticmethod
    def get_academy_name(obj):
        return obj.academy.name

    def validate(self, attrs):
        AcademyFrequency.objects.filter(day_week=attrs['day_week'])

        user = User.objects.get(username=attrs['username'])
        exists_user_academy = UserAcademy.objects.filter(user=user, academy=attrs['academy']).exists()

        conflicts = AcademyFrequency.objects.filter(
            day_week=attrs['day_week'],
            academy=attrs['academy']
        ).filter(
            start__lt=attrs['end'],
            end__gt=attrs['start']
        )

        if exists_user_academy and conflicts.exists():
            frequency_conflict = conflicts.first()
            raise serializers.ValidationError(f'Houve um conflito com {frequency_conflict.academy.name} frequentada {frequency_conflict.get_day_week_display()} das {frequency_conflict.start} até {frequency_conflict.end}', code=status.HTTP_400_BAD_REQUEST)

        return attrs

    def create(self, validated_data):
        academy = validated_data['academy']
        user = User.objects.get(username=validated_data.pop('username'))

        if not UserAcademy.objects.filter(user=user, academy=academy).exists():
            UserAcademy.objects.create(user=user, academy=academy)

        return super().create(validated_data)

class ListAcademiesSerializer(serializers.ModelSerializer):
    class Meta:
        model = Academy
        fields = ['id', 'name']


class UserAuthenticationSerializer(serializers.Serializer):
    username = serializers.CharField(allow_null=True, allow_blank=True)
    password = serializers.CharField(allow_null=True, allow_blank=True)

    @staticmethod
    def validate_username(username: str):
        if username is None or len(username) == 0:
            raise serializers.ValidationError('O nome de usuário é obrigatório.')

        return username

    @staticmethod
    def validate_password(password: str):
        if password is None or len(password) == 0:
            raise serializers.ValidationError('A senha é obrigatória.')

        return password
