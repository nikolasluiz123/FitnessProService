from django.contrib.auth.models import User, Group
from email_validator import validate_email, EmailNotValidError
from rest_framework import serializers
from django.contrib.auth.hashers import make_password

from authentication.enumerators import EnumUserProfile
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

    class Meta:
        model = AcademyFrequency
        fields = ['id', 'day_week', 'day_week_display', 'start', 'end']

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


class AcademySerializer(serializers.ModelSerializer):
    frequencies = AcademyFrequencySerializer(many=True)
    username = serializers.CharField(write_only=True)

    class Meta:
        model = Academy
        fields = ['id', 'name', 'frequencies', 'username']

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        self.fields['name'].error_messages = {
            'required': 'O nome é obrigatório.',
            'null': 'O nome é obrigatório.',
            'blank': 'O nome é obrigatório.'
        }

        self.fields['frequencies'].error_messages = {
            'required': 'A frequência é obrigatória.',
            'null': 'A frequência é obrigatória.',
            'blank': 'A frequência é obrigatória.'
        }

    @staticmethod
    def validate_frequencies(frequencies):
        print(frequencies)

        if len(frequencies) == 0:
            raise serializers.ValidationError('É obrigatório informar ao menos um dia da semana na frequência da '
                                              'academia.')

        return frequencies

    def create(self, validated_data):
        frequencies_data = validated_data.pop('frequencies')
        username = validated_data.pop('username')

        academy = Academy.objects.create(**validated_data)

        for frequency_data in frequencies_data:
            AcademyFrequency.objects.create(academy=academy, **frequency_data)

        user = User.objects.get(username=username)
        UserAcademy.objects.create(user=user, academy=academy)

        return academy

    def update(self, instance, validated_data):
        frequencies_data = validated_data.pop('frequencies')
        instance.name = validated_data.get('name', instance.name)
        instance.save()

        for frequency_data in frequencies_data:
            frequency_id = frequency_data.get('id')

            if frequency_id:
                frequency = AcademyFrequency.objects.get(id=frequency_id, academy=instance)
                frequency.day_week = frequency_data.get('day_week', frequency.day_week)
                frequency.start = frequency_data.get('start', frequency.start)
                frequency.end = frequency_data.get('end', frequency.end)
                frequency.save()
            else:
                AcademyFrequency.objects.create(academy=instance, **frequency_data)

        return instance


class ListAcademiesSerializer(serializers.ModelSerializer):
    class Meta:
        model = Academy
        fields = ['id', 'name']
