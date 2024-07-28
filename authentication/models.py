from django.contrib.auth.models import User
from django.db import models
from django.db.models import CASCADE, Case, When, IntegerField


class Academy(models.Model):
    """
        Entidade para representar a Academia que o Aluno frequenta ou o Intrutor / Nutricionista trabalham
    """

    name = models.CharField(max_length=256, null=False, blank=False)

    def __str__(self):
        return self.name


class AcademyFrequency(models.Model):
    """
        Entidade para representar a frequência que o Aluno, Instrutor ou Nutricionista vão estar presentes na Academia.
    """

    DAY_WEEK = [
        ('SEG', 'Segunda'),
        ('TER', 'Terça'),
        ('QUA', 'Quarta'),
        ('QUI', 'Quinta'),
        ('SEX', 'Sexta'),
        ('SAB', 'Sábado'),
        ('DOM', 'Domingo')
    ]

    day_week = models.CharField(max_length=3, choices=DAY_WEEK, blank=False, null=False)
    start = models.TimeField(null=False, blank=False)
    end = models.TimeField(null=False, blank=False)
    academy = models.ForeignKey(Academy, on_delete=CASCADE, related_name='frequencies', null=False, blank=False)

    @staticmethod
    def get_day_week_ordering_case():
        return Case(
            When(day_week='DOM', then=0),
            When(day_week='SEG', then=1),
            When(day_week='TER', then=2),
            When(day_week='QUA', then=3),
            When(day_week='QUI', then=4),
            When(day_week='SEX', then=5),
            When(day_week='SAB', then=6),
            output_field=IntegerField(),
        )


class UserAcademy(models.Model):
    """
        Entidade para possibilitar referenciar quais academias o usuário definiu que frequenta.
    """

    user = models.ForeignKey(User, on_delete=CASCADE, null=False, blank=False)
    academy = models.ForeignKey(Academy, on_delete=CASCADE, null=False, blank=False)
